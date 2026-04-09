package co.istad.y2.quizzy.config.WebSocket;

import co.istad.y2.quizzy.service.RoomService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private final RoomService roomService;
    public WebSocketEventListener(RoomService roomService) {
        this.roomService = roomService;
    }
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() != null) {
            String username = accessor.getUser().getName();
            System.out.println(">>> User disconnected: " + username);
            roomService.removePlayerFromAllRooms(username);
        }
    }
}