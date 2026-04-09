package co.istad.y2.quizzy.config.WebSocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class QuizRoom {
    private String roomCode;
    private Long quizId;
    private String owner;
    private Set<String> participants = new HashSet<>();
    private boolean started = false;
}