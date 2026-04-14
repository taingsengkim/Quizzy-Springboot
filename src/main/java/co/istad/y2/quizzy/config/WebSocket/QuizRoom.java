package co.istad.y2.quizzy.config.WebSocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;

@Getter
@Setter
@ToString
public class QuizRoom {
    private String roomCode;
    private Long quizId;
    private String owner;
    private Set<String> participants = new HashSet<>();
    private boolean started = false;
    private boolean finished = false;

    private Map<String, Integer> scores = new HashMap<>();
    private Map<String, Long> finishTime = new HashMap<>();
    // Per-player current question index
    private Map<String, Integer> playerQuestionIndex = new HashMap<>();
    // Per-player answered questions: username -> set of question indices answered
    private Map<String, Set<Integer>> playerAnsweredQuestions = new HashMap<>();
    // Per-player finished state
    private Set<String> finishedPlayers = new HashSet<>();
    // Per-player current question (what each player sees)
    private Map<String, CurrentQuestion> playerCurrentQuestion = new HashMap<>();
    @Getter
    @Setter
    public static class CurrentQuestion {
        private Long id;
        private String text;
        private List<Answer> answers;
        private int questionIndex;
        private String code;
        private String questionType;
        private Integer points;
        private String difficulty;
    }

    @Getter
    @Setter
    public static class Answer {
        private Long id;
        private String text;
        public Answer(Long id, String text) {
            this.id = id;
            this.text = text;
        }
    }
}