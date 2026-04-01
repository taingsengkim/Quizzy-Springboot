package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.mapper.QuizMapper;
import co.istad.y2.quizzy.model.Answer;
import co.istad.y2.quizzy.model.Difficulty;
import co.istad.y2.quizzy.model.Question;
import co.istad.y2.quizzy.model.Quiz;
import co.istad.y2.quizzy.repository.CategoryRepository;
import co.istad.y2.quizzy.repository.QuestionRepository;
import co.istad.y2.quizzy.repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final QuizMapper quizMapper;
    public QuizServiceImpl(QuizRepository quizRepository,
                           QuestionRepository questionRepository,
                           CategoryRepository categoryRepository,
                           QuizMapper quizMapper){
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.quizMapper = quizMapper;
    }


    @Override
    @Transactional
    public QuizResponseDto createQuiz(QuizCreateDto quizDto) {
        System.out.println(quizDto);
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDto.title());
        quiz.setDescription(quizDto.description());
        quiz.setDuration(quizDto.duration());
        if (quizDto.categoryId() != null) {
            categoryRepository.findById(quizDto.categoryId())
                    .ifPresent(quiz::setCategory);
        }
        if (quizDto.questions() != null) {
            List<Question> questionList = quizDto.questions().stream().map(qDto -> {
                Question q = new Question();
                q.setText(qDto.text());
                q.setQuestionType(qDto.questionType());
                q.setPoints(qDto.points());
                q.setDifficulty(Difficulty.valueOf(qDto.difficulty()));

                // Map answers
                if (qDto.answers() != null) {
                    List<Answer> answers = qDto.answers().stream().map(aDto -> {
                        Answer a = new Answer();
                        a.setText(aDto.text());
                        a.setCorrect(aDto.correct());
                        a.setQuestion(q); // link answer to question
                        return a;
                    }).toList();
                    q.setAnswers(answers);
                }
                q.setQuiz(quiz); // link question to quiz
                return q;
            }).toList();

            quiz.setQuestions(questionList);
        }
        Quiz save = quizRepository.save(quiz);
        return quizMapper.mapToResponse(save);
    }

    @Override
    public Quiz updateQuiz(Long id, Quiz quiz) {
        return null;
    }

    @Override
    public void deleteQuiz(Long id) {

    }

    @Override
    public Optional<Quiz> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<QuizResponseDto> findAll() {
        return quizRepository.findAllWithQuestions().stream()
                .map(quizMapper::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<Quiz> findByCategory(Long categoryId) {
        return List.of();
    }
}
