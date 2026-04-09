package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.config.WebSocket.QuizRoom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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

        if (room != null) {
            if (!room.getParticipants().contains(username)) {
                room.getParticipants().add(username);
            }
        }

        return room;
    }
    public QuizRoom startRoom(String roomCode, String username) {
        QuizRoom room = rooms.get(roomCode);

        if (room != null) {
            if (room.getOwner().equals(username)) {
                room.setStarted(true);
            } else {
                log.info("{} is not the owner", username);
            }
        }

        return room;
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
                if (room.getOwner().equals(username)) {
                    if (!room.getParticipants().isEmpty()) {
                        String newOwner = room.getParticipants().iterator().next();
                        room.setOwner(newOwner);
                    }
                }
                if (room.getParticipants().isEmpty()) {
                    iterator.remove();
                    log.info("Room {} deleted (empty)", room.getRoomCode());
                    continue;
                }
                messagingTemplate.convertAndSend(
                        "/topic/room/" + room.getRoomCode(),
                        room
                );
            }
        }
    }
    private String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}