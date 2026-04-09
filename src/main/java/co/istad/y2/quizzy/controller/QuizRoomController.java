package co.istad.y2.quizzy.controller;

import co.istad.y2.quizzy.config.WebSocket.QuizRoom;
import co.istad.y2.quizzy.dto.multiplayer.RoomMessage;
import co.istad.y2.quizzy.model.Quiz;
import co.istad.y2.quizzy.service.RoomService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class QuizRoomController {
    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;
    public QuizRoomController(RoomService roomService,
                              SimpMessagingTemplate simpMessagingTemplate) {
        this.roomService = roomService;
        this.messagingTemplate = simpMessagingTemplate;
    }
    @MessageMapping("/create-room")
    public void createRoom(@Payload RoomMessage message) {
        System.out.println(">>> create-room received: " + message.username() + ", quizId: " + message.quizId());

        QuizRoom room = roomService.createRoom(message.quizId(), message.username());
        System.out.println(">>> room created: " + room);

        if (room != null) {
            System.out.println(">>> sending to user: " + message.username() + " → /queue/reply");
            messagingTemplate.convertAndSendToUser(
                    message.username(),
                    "/queue/reply",
                    room
            );
            System.out.println(">>> sent!");
        } else {
            System.out.println(">>> room is NULL — createRoom service returned null");
        }
    }
    @MessageMapping("/join-room")
    public void joinRoom(@Payload RoomMessage message) {
        QuizRoom room = roomService.joinRoom(message.roomCode(), message.username());
        if (room != null) {
            messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
        } else {
            System.err.println("Join failed: Room " + message.roomCode() + " not found.");
        }
    }
    @MessageMapping("/start-room")
    public void startRoom(@Payload RoomMessage message) {
        QuizRoom room = roomService.startRoom(message.roomCode(), message.username());
        if (room != null) {
            messagingTemplate.convertAndSend("/topic/room/" + room.getRoomCode(), room);
        }
    }
}