package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.config.WebSocket.QuizRoom;
import co.istad.y2.quizzy.dto.answer.AnswerPlayDto;
import co.istad.y2.quizzy.dto.question.QuestionPlayDto;
import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class RoomService {

    private final Map<String, QuizRoom> rooms = new HashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    public RoomService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public QuizRoom createRoom(Long quizId, String owner) {
        QuizRoom room = new QuizRoom();
        room.setRoomCode(generateRoomCode());
        room.setQuizId(quizId);
        room.setOwner(owner);
        room.getParticipants().add(owner);
        rooms.put(room.getRoomCode(), room);
        return room;
    }

    public QuizRoom joinRoom(String roomCode, String username) {
        QuizRoom room = rooms.get(roomCode);
        if (room != null && !room.getParticipants().contains(username)) {
            room.getParticipants().add(username);
        }
        return room;
    }

    public QuizRoom startRoom(String roomCode, String username, QuizService quizService) {
        QuizRoom room = rooms.get(roomCode);
        if (room != null && room.getOwner().equals(username)) {
            room.setStarted(true);
            room.setStartedAt(System.currentTimeMillis());
            // Initialize every player at question index 0
            for (String participant : room.getParticipants()) {
                room.getPlayerQuestionIndex().put(participant, 0);
                room.getPlayerAnsweredQuestions().put(participant, new HashSet<>());
                pushQuestionToPlayer(room, participant, quizService);
            }
        }
        return room;
    }

    // Push the current question to a specific player
    public void pushQuestionToPlayer(QuizRoom room, String username, QuizService quizService) {
        int index = room.getPlayerQuestionIndex().getOrDefault(username, 0);
        int total = quizService.totalQuestions(room.getQuizId());

        if (index >= total) {
            // This player finished all questions
            room.getFinishedPlayers().add(username);
            room.getFinishTime().put(username, System.currentTimeMillis());
            room.getPlayerCurrentQuestion().remove(username);
            // If ALL players finished -> mark room finished
            if (room.getFinishedPlayers().containsAll(room.getParticipants())) {
                room.setFinished(true);
                log.info("All players finished — room {} complete", room.getRoomCode());
            }
            return;
        }
        QuizPlayResponseDto quiz = quizService.getQuizForPlay(room.getQuizId());
        QuestionPlayDto q = quiz.questions().get(index);
        QuizRoom.CurrentQuestion cq = new QuizRoom.CurrentQuestion();
        cq.setId(q.id());
        cq.setText(q.text());
        cq.setCode(q.code());
        cq.setQuestionType(q.questionType());
        cq.setPoints(q.points());
        cq.setDifficulty(q.difficulty());
        cq.setQuestionIndex(index);
        log.info("TYPE = {}, POINTS = {}, DIFF = {}",
                q.questionType(),
                q.points(),
                q.difficulty()
        );
        List<QuizRoom.Answer> answers = new ArrayList<>();
        for (AnswerPlayDto a : q.answers()) {
            answers.add(new QuizRoom.Answer(a.id(), a.text()));
        }
        cq.setAnswers(answers);
        room.getPlayerCurrentQuestion().put(username, cq);
        log.info("Pushed question {} to player {}", index, username);
    }

    public QuizRoom getRoom(String roomCode) {
        return rooms.get(roomCode);
    }

    public void removePlayerFromAllRooms(String username) {
        Iterator<Map.Entry<String, QuizRoom>> iterator = rooms.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, QuizRoom> entry = iterator.next();
            QuizRoom room = entry.getValue();
            boolean removed = room.getParticipants().removeIf(p -> p.equals(username));
            if (removed) {
                log.info("Removed {} from room {}", username, room.getRoomCode());
                if (room.getOwner().equals(username) && !room.getParticipants().isEmpty()) {
                    String newOwner = room.getParticipants().iterator().next();
                    room.setOwner(newOwner);
                }
                if (room.getParticipants().isEmpty()) {
                    iterator.remove();
                    log.info("Room {} deleted (empty)", room.getRoomCode());
                    continue;
                }
                messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
            }
        }
    }

    private String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}