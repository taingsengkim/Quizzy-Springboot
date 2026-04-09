package co.istad.y2.quizzy.dto.multiplayer;

public record RoomMessage(
        String roomCode,
        Long quizId,
        String username
){
}
