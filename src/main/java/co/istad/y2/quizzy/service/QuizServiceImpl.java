package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.answer.AnswerPlayDto;
import co.istad.y2.quizzy.dto.answer.AnswerResponseDto;
import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.question.QuestionPlayDto;
import co.istad.y2.quizzy.dto.question.QuestionResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizCreateDto;
import co.istad.y2.quizzy.dto.quiz.QuizPlayResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizResponseDto;
import co.istad.y2.quizzy.dto.quiz.QuizUpdateDto;
import co.istad.y2.quizzy.mapper.CategoryMapper;
import co.istad.y2.quizzy.mapper.QuizMapper;
import co.istad.y2.quizzy.model.*;
import co.istad.y2.quizzy.repository.CategoryRepository;
import co.istad.y2.quizzy.repository.QuestionRepository;
import co.istad.y2.quizzy.repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final QuizMapper quizMapper;
    private final CategoryMapper categoryMapper;
    private final Map<String, Map<Long, Integer>> hintUsage = new HashMap<>();
    public QuizServiceImpl(QuizRepository quizRepository,
                           QuestionRepository questionRepository,
                           CategoryRepository categoryRepository,
                           QuizMapper quizMapper, CategoryMapper categoryMapper){
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.quizMapper = quizMapper;
        this.categoryMapper = categoryMapper;
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
                q.setCode(qDto.code());
                q.setHint(qDto.hint());
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
    @Transactional
    public QuizResponseDto updateQuiz(Long id, QuizUpdateDto dto) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz Not Found!"));
        if (dto.title() != null) {
            quiz.setTitle(dto.title());
        }
        if (dto.description() != null) {
            quiz.setDescription(dto.description());
        }
        if (dto.duration() != null) {
            quiz.setDuration(dto.duration());
        }
        if (dto.categoryId() != null) {
            Category category = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found!"));
            quiz.setCategory(category);
        }
        Quiz updated = quizRepository.save(quiz);
        return quizMapper.mapToResponse(updated);
    }

    @Override
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id).
                orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Quiz Not Found!"));
        quizRepository.delete(quiz);
    }

    @Override
    public QuizResponseDto findById(Long id) {
        return quizMapper.mapToResponse(quizRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Quiz Not Found!")));
    }

    @Override
    public Page<QuizResponseDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return quizRepository.findAll(pageable)
                .map(quizMapper::mapToResponse);
    }

    @Override
    @Transactional
    public QuizPlayResponseDto getQuizForPlay(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz Not Found"));
        if (quiz.getQuestions() == null || quiz.getQuestions().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This quiz has no questions.");
        }
        return new QuizPlayResponseDto(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getDuration(),
                quiz.getCategory() != null ? quiz.getCategory().getId() : null,
                quiz.getQuestions().stream().map(q ->
                        new QuestionPlayDto(
                                q.getId(),
                                q.getText(),
                                q.getAnswers().stream()
                                        .map(a -> new AnswerPlayDto(a.getId(), a.getText()))
                                        .toList(),
                                q.getQuestionType().name(),
                                q.getPoints(),
                                q.getCode(),
                                q.getDifficulty() != null ? q.getDifficulty().name() : null,
                                q.getHint()
                        )
                ).toList()
        );
    }

    @Override
    public Page<QuizResponseDto> findByCategoryId(Long categoryId, int page, int size) {

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category Not Found!"
                ));

        Pageable pageable = PageRequest.of(page, size);

        return quizRepository.findByCategoryId(categoryId, pageable)
                .map(quiz -> new QuizResponseDto(
                        quiz.getId(),
                        quiz.getTitle(),
                        quiz.getDescription(),
                        quiz.getDuration(),
                        quiz.getCategory() != null ? quiz.getCategory().getId() : null,
                        quiz.getQuestions().stream().map(q ->
                                new QuestionResponseDto(
                                        q.getId(),
                                        q.getText(),
                                        q.getAnswers().stream()
                                                .map(a -> new AnswerResponseDto(
                                                        a.getId(),
                                                        a.getText(),
                                                        a.isCorrect()
                                                ))
                                                .toList(),
                                        q.getQuestionType(),
                                        q.getPoints(),
                                        q.getCode(),
                                        q.getDifficulty(),
                                        q.getHint()
                                )
                        ).toList()
                ));
    }
    @Override
    @Transactional
    public boolean isCorrectAnswer(Long quizId, int questionIndex, String userAnswer) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz Not Found"));

        List<Question> questions = quiz.getQuestions();
        if (questionIndex < 0 || questionIndex >= questions.size()) return false;

        Question question = questions.get(questionIndex);
        return question.getAnswers().stream()
                .filter(Answer::isCorrect)
                .anyMatch(a -> a.getText().equalsIgnoreCase(userAnswer.trim()));
    }

    @Override
    @Transactional
    public int totalQuestions(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz Not Found"));
        return quiz.getQuestions().size();
    }

    public String getHint(Long quizId, Long questionId, String attemptId) {

        // get attempt map
        Map<Long, Integer> attemptHints =
                hintUsage.computeIfAbsent(attemptId, k -> new HashMap<>());

        int used = attemptHints.getOrDefault(questionId, 0);

        if (used >= 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You can only use 2 hints per question"
            );
        }

        // increment usage
        attemptHints.put(questionId, used + 1);

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz Not Found"));

        Question question = quiz.getQuestions().stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Not Found"));

        return question.getHint();
    }
    @Override
    public void resetHint(String attempId) {
        hintUsage.remove(attempId);
    }

    @Override
    public String startAttempt(Long quizId) {

        // check quiz exists (optional but good)
        quizRepository.findById(quizId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Quiz Not Found"));

        // generate unique attemptId
        String attemptId = java.util.UUID.randomUUID().toString();

        // initialize hint tracking
        hintUsage.put(attemptId, new HashMap<>());

        return attemptId;
    }

}
