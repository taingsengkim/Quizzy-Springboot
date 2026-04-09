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
        log.info("answer-question: user={} roomCode={} answer={}", message.username(), message.roomCode(), message.answer());
        QuizRoom room = roomService.getRoom(message.roomCode());
        if (room == null || !room.isStarted()) return;
        String username = message.username();
        // If this player already finished all questions ignore
        if (room.getFinishedPlayers().contains(username)) return;
        int myIndex = room.getPlayerQuestionIndex().getOrDefault(username, 0);
        // Prevent answering same question twice
        room.getPlayerAnsweredQuestions().computeIfAbsent(username, k -> new java.util.HashSet<>());
        if (room.getPlayerAnsweredQuestions().get(username).contains(myIndex)) return;
        room.getPlayerAnsweredQuestions().get(username).add(myIndex);
        // Check correctness
        QuizPlayResponseDto quiz = quizService.getQuizForPlay(room.getQuizId());
        QuestionPlayDto question = quiz.questions().get(myIndex);
        boolean correct = question.answers().stream()
                .anyMatch(a -> a.id().equals(Long.parseLong(message.answer()))
                        && quizService.isCorrectAnswer(room.getQuizId(), myIndex, a.text()));
        if (correct) {
            room.getScores().merge(username, 1, Integer::sum);
            log.info("User {} correct on q{} — score: {}", username, myIndex, room.getScores().get(username));
        }
        // Advance THIS player to next question
        room.getPlayerQuestionIndex().put(username, myIndex + 1);
        roomService.pushQuestionToPlayer(room, username, quizService);

        // Broadcast updated room state to everyone
        // Each player's frontend reads playerCurrentQuestion[their username]
        messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
    }
}