package co.istad.y2.quizzy.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;


    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
