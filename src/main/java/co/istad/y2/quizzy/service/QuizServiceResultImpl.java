package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz_result.*;
import co.istad.y2.quizzy.model.*;
import co.istad.y2.quizzy.repository.QuestionRepository;
import co.istad.y2.quizzy.repository.QuizRepository;
import co.istad.y2.quizzy.repository.QuizResultRepository;
import co.istad.y2.quizzy.repository.UserAnswerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizServiceResultImpl implements QuizServiceResult {

    private final QuestionRepository questionRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizRepository quizRepository;
    private final UserAnswerRepository userAnswerRepository;

    public QuizServiceResultImpl(
            QuestionRepository questionRepository,
            QuizResultRepository quizResultRepository,
            QuizRepository quizRepository,
            UserAnswerRepository userAnswerRepository) {
        this.questionRepository = questionRepository;
        this.quizResultRepository = quizResultRepository;
        this.quizRepository = quizRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    @Override
    public QuizResultResponseDto submitQuiz(SubmitQuizDto submitQuizDto, User user) {
        Quiz quiz = quizRepository.findById(submitQuizDto.quizId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found!"));
        int score = 0;
        int total = submitQuizDto.answers().size();
        QuizResult result = new QuizResult();
        result.setUser(user);
        result.setQuiz(quiz);
        result.setCreated(Instant.now());
        result.setDuration(submitQuizDto.duration());
        result.setTotal(total);
        quizResultRepository.save(result);
        // Add the result to the user's in-memory quiz list
        if (user.getQuizResults() != null) {
            user.getQuizResults().add(result);
        }
        for (SubmitAnswerDto submitAnswerDto : submitQuizDto.answers()) {
            Question question = questionRepository.findById(submitAnswerDto.questionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Not Found!"));
            Set<Long> correctIds = question.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .map(Answer::getId)
                    .collect(Collectors.toSet());

            Set<Long> userIds = new HashSet<>(submitAnswerDto.answerId());

            if (correctIds.equals(userIds)) {
                score++;
            }
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUser(user);
            userAnswer.setQuizResult(result);
            userAnswer.setQuestion(question);
            userAnswer.setSelectedAnswers(
                    question.getAnswers().stream()
                            .filter(a -> submitAnswerDto.answerId().contains(a.getId()))
                            .toList()
            );

            userAnswerRepository.save(userAnswer);
        }
        result.setScore(score);
        return new QuizResultResponseDto(score, total,result.getId());
    }

    @Override
    public List<QuizResultHistoryDto> getUserHistory(User user) {
        return quizResultRepository.findByUser(user).stream()
                .map(r -> new QuizResultHistoryDto(
                        r.getId(),
                        r.getQuiz().getTitle(),
                        r.getScore(),
                        r.getTotal(),
                        r.getDuration()
                ))
                .toList();
    }

    @Override
    public QuizResultDetailDto getResultDetail(Long resultId, User user) {

        QuizResult result = quizResultRepository.findById(resultId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Result not found"));

        // security check
        if (!result.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        List<UserAnswer> userAnswers = userAnswerRepository.findByQuizResult(result);

        List<QuestionResultDto> questions = userAnswers.stream().map(ua -> {

            List<String> correctAnswers = ua.getQuestion().getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .map(Answer::getText)
                    .toList();

            List<String> userSelected = ua.getSelectedAnswers().stream()
                    .map(Answer::getText)
                    .toList();

            return new QuestionResultDto(
                    ua.getQuestion().getId(),
                    ua.getQuestion().getText(),
                    correctAnswers,
                    userSelected
            );

        }).toList();

        return new QuizResultDetailDto(
                result.getId(),
                result.getQuiz().getTitle(),
                result.getScore(),
                result.getTotal(),
                questions
        );
    }
}