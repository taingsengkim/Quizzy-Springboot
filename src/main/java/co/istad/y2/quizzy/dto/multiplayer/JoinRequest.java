package co.istad.y2.quizzy.dto.multiplayer;

public record JoinRequest(
        String roomCode,
        String username
) {
}
