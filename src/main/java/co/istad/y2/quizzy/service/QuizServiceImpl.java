package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz.*;
import co.istad.y2.quizzy.exception.question.QuestionNotFoundException;
import co.istad.y2.quizzy.model.*;
import co.istad.y2.quizzy.repository.QuestionRepository;
import co.istad.y2.quizzy.repository.QuizResultRepository;
import co.istad.y2.quizzy.repository.UserAnswerRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class QuizServiceImpl implements QuizService{
    private final QuestionRepository questionRepository;
    private final QuizResultRepository quizResultRepository;
    public QuizServiceImpl(
            QuestionRepository questionRepository,
            QuizResultRepository quizResultRepository
    ){
        this.questionRepository = questionRepository;
        this.quizResultRepository = quizResultRepository;
    }

    @Override
    public QuizResultResponseDto submitQuiz(SubmitQuizDto submitQuizDto, User user) {
        int score = 0 ;
        int total = submitQuizDto.answers().size();

        for(SubmitAnswerDto submitAnswerDto : submitQuizDto.answers()) {
            Question question = questionRepository.findById(submitAnswerDto.questionId())
                    .orElseThrow(() -> new QuestionNotFoundException("Question Not Found!"));

            Set<Long> correctId = question.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .map(Answer::getId)
                    .collect(Collectors.toSet());

            Set<Long> userIds = new HashSet<>(submitAnswerDto.answerId());

            if (correctId.equals(userIds)) {
                score++;
            }
        }
            QuizResult result = new QuizResult();
            result.setUser(user);
            result.setScore(score);
            result.setTotal(total);

            quizResultRepository.save(result);
            return new QuizResultResponseDto(score,total);
    }

    @Override
    public List<QuizQuestionsDto> getQuizByCategory(Long categoryId) {
       List<Question> questions = questionRepository.findByCategoryId(categoryId);

        return questions.stream().map(
                q-> new QuizQuestionsDto(
                    q.getId(),
                        q.getText(),
                        q.getAnswers().stream()
                                .map(a->new QuizAnswerDto(a.getId(),a.getText())).toList()
                )
        ).toList();
    }
}
