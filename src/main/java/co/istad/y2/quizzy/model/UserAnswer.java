package co.istad.y2.quizzy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private QuizResult quizResult;

    @ManyToOne
    private Question question;

    @ManyToMany
    private List<Answer> selectedAnswers;

}
