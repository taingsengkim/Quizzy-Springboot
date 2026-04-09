package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.answer.AnswerPlayDto;
import co.istad.y2.quizzy.dto.category.CategoryResponseDto;
import co.istad.y2.quizzy.dto.question.QuestionPlayDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService{
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final QuizMapper quizMapper;
    private final CategoryMapper categoryMapper;

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
    public List<QuizResponseDto> findAll() {
        return quizRepository.findAllWithQuestions().stream()
                .map(quizMapper::mapToResponse).collect(Collectors.toList());
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
                quiz.getQuestions().stream().map(q ->
                        new QuestionPlayDto(
                                q.getId(),
                                q.getText(),
                                q.getAnswers().stream()
                                        .map(a -> new AnswerPlayDto(a.getId(), a.getText()))
                                        .toList(),
                                q.getQuestionType().name(),
                                q.getPoints()
                        )
                ).toList(),
                quiz.getDuration()
        );
    }
    @Override
    public List<QuizPlayResponseDto> findByCategoryId(Long categoryId) {
        CategoryResponseDto category = categoryMapper.mapToResponse(categoryRepository.findById(categoryId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found!")));

        List<Quiz> quizzes = quizRepository.findByCategoryId(categoryId);
        List<QuizPlayResponseDto> quizDtos = quizzes.stream()
                .map(quiz ->
                        new QuizPlayResponseDto(
                            quiz.getId(),
                                quiz.getTitle(),
                                quiz.getQuestions().stream().map(q ->
                                        new QuestionPlayDto(
                                                q.getId(),
                                                q.getText(),
                                                q.getAnswers().stream()
                                                        .map(a -> new AnswerPlayDto(a.getId(), a.getText()))
                                                        .toList(),
                                                q.getQuestionType().name(),
                                                q.getPoints()
                                        )
                        ).toList(), quiz.getDuration()
                )).toList();

        return quizDtos;
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


}
