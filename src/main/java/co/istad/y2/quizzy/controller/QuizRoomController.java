package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.config.WebSocket.QuizRoom;
import co.istad.y2.quizzy.dto.multiplayer.RoomMessage;
import co.istad.y2.quizzy.dto.question.QuestionPlayDto;
import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import co.istad.y2.quizzy.service.QuizService;
import co.istad.y2.quizzy.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class QuizRoomController {

    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final QuizService quizService;

    public QuizRoomController(RoomService roomService,
                              SimpMessagingTemplate messagingTemplate,
                              QuizService quizService) {
        this.roomService = roomService;
        this.messagingTemplate = messagingTemplate;
        this.quizService = quizService;
    }

    @MessageMapping("/create-room")
    public void createRoom(@Payload RoomMessage message) {
        log.info("create-room: user={} quizId={}", message.username(), message.quizId());
        QuizRoom room = roomService.createRoom(message.quizId(), message.username());
        if (room != null) {
            messagingTemplate.convertAndSendToUser(
                    message.username(), "/queue/reply", room
            );
        }
    }

    @MessageMapping("/join-room")
    public void joinRoom(@Payload RoomMessage message) {
        log.info("join-room: user={} roomCode={}", message.username(), message.roomCode());
        QuizRoom room = roomService.joinRoom(message.roomCode(), message.username());
        if (room != null) {
            messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
        }
    }

    @MessageMapping("/start-room")
    public void startRoom(@Payload RoomMessage message) {
        log.info("start-room: user={} roomCode={}", message.username(), message.roomCode());
        QuizRoom room = roomService.startRoom(message.roomCode(), message.username(), quizService);
        if (room != null) {
            messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
        }
    }

    @MessageMapping("/answer-question")
    public void answerQuestion(@Payload RoomMessage message) {
        log.info("answer-question: user={} roomCode={} answer={}",
                message.username(), message.roomCode(), message.answer());

        QuizRoom room = roomService.getRoom(message.roomCode());
        if (room == null || !room.isStarted()) return;

        String username = message.username();
        if (room.getFinishedPlayers().contains(username)) return;

        int myIndex = room.getPlayerQuestionIndex().getOrDefault(username, 0);

        room.getPlayerAnsweredQuestions().computeIfAbsent(username, k -> new HashSet<>());
        if (room.getPlayerAnsweredQuestions().get(username).contains(myIndex)) return;
        room.getPlayerAnsweredQuestions().get(username).add(myIndex);

        QuizPlayResponseDto quiz = quizService.getQuizForPlay(room.getQuizId());
        QuestionPlayDto question = quiz.questions().get(myIndex);

        String[] answerIds = message.answer().split(",");
        Set<Long> selectedIds = Arrays.stream(answerIds)
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        Set<Long> correctIds = question.answers().stream()
                .filter(a -> quizService.isCorrectAnswer(room.getQuizId(), myIndex, a.text()))
                .map(a -> a.id())
                .collect(Collectors.toSet());

        boolean correct = selectedIds.equals(correctIds);

        if (correct) {
            room.getScores().merge(username, question.points() != null ? question.points() : 1, Integer::sum);
            log.info("User {} correct on q{} — score: {}", username, myIndex, room.getScores().get(username));
        }
        //Record answer history
        QuizRoom.AnswerResult result = new QuizRoom.AnswerResult();
        result.setQuestionIndex(myIndex);
        result.setQuestionText(question.text());
        result.setCorrect(correct);
        result.setPoints(correct && question.points() != null ? question.points() : 0);

        result.setSelectedAnswerTexts(
                question.answers().stream()
                        .filter(a -> selectedIds.contains(a.id()))
                        .map(a -> a.text())
                        .toList()
        );
        result.setCorrectAnswerTexts(
                question.answers().stream()
                        .filter(a -> correctIds.contains(a.id()))
                        .map(a -> a.text())
                        .toList()
        );

        room.getPlayerAnswerHistory()
                .computeIfAbsent(username, k -> new ArrayList<>())
                .add(result);
        // Advance to next question
        room.getPlayerQuestionIndex().put(username, myIndex + 1);
        roomService.pushQuestionToPlayer(room, username, quizService);
        messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
    }

    @MessageMapping("/time-up")
    public void timeUp(@Payload RoomMessage message) {
        log.info("time-up: roomCode={}", message.roomCode());
        QuizRoom room = roomService.getRoom(message.roomCode());
        if (room == null || !room.isStarted() || room.isFinished()) return;

        // Force-finish all players who haven't finished yet
        for (String participant : room.getParticipants()) {
            if (!room.getFinishedPlayers().contains(participant)) {
                room.getFinishedPlayers().add(participant);
                room.getFinishTime().put(participant, System.currentTimeMillis());
                room.getPlayerCurrentQuestion().remove(participant);
            }
        }

        room.setFinished(true);
        messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
    }


}