package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.answer.AnswerResponseDto;
import co.istad.y2.quizzy.dto.question.CreateQuestionDto;
import co.istad.y2.quizzy.dto.question.QuestionResponseDto;
import co.istad.y2.quizzy.dto.question.UpdateQuestionDto;
import co.istad.y2.quizzy.mapper.QuestionMapper;
import co.istad.y2.quizzy.model.*;
import co.istad.y2.quizzy.repository.AnswerRepository;
import co.istad.y2.quizzy.repository.QuestionRepository;
import co.istad.y2.quizzy.repository.QuizRepository;
import co.istad.y2.quizzy.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               AnswerRepository answerRepository,
                               UserRepository userRepository,
                               QuizRepository quizRepository,
                               QuestionMapper questionMapper){
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public QuestionResponseDto createQuestion(CreateQuestionDto createQuestionDto,User user){
        List<Answer> answers = createQuestionDto.answers().stream().map(
                a->{
                    Answer ans = new Answer();
                    ans.setText(a.text());
                    ans.setCorrect(a.correct());
                    return ans;
                }
        ).toList();
        Quiz quiz = quizRepository.findById(createQuestionDto.quizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Question question = new Question();
        question.setText(createQuestionDto.text());
        question.setCode(createQuestionDto.code());
        question.setHint(createQuestionDto.hint());
        answers.forEach(a->a.setQuestion(question));
        question.setAnswers(answers);
        question.setQuiz(quiz);
        question.setQuestionType(createQuestionDto.questionType());
        question.setPoints(createQuestionDto.points());
        question.setDifficulty(createQuestionDto.difficulty());

        if (createQuestionDto.questionType() == QuestionType.SINGLE_CHOICE ||
                createQuestionDto.questionType() == QuestionType.TRUE_FALSE) {

            long correctCount = answers.stream()
                    .filter(Answer::isCorrect)
                    .count();

            if (correctCount != 1) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Only 1 correct answer is allowed for this question type"
                );
            }
        }


        Question saved = questionRepository.save(question);
        List<AnswerResponseDto> answerResponseDtos = saved.getAnswers().stream()
                .map(a->new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect())).toList();


        return questionMapper.mapToResponse(saved);
    }



    @Override
    public List<QuestionResponseDto> getAllQuestions(){
        return questionRepository.findAll().stream().map(q->{
            List<AnswerResponseDto> answerResponseDtos = q.getAnswers().stream()
                    .map(a-> new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect()))
                    .toList();
             return questionMapper.mapToResponse(q);
        }).toList();
    }

    @Override
    @Transactional
    public QuestionResponseDto updateQuestion(Long id, UpdateQuestionDto dto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Question Not Found!"));
        if (dto.text() != null) question.setText(dto.text());
        if (dto.code() != null) question.setCode(dto.code());
        if (dto.hint() != null) question.setHint(dto.hint());
        if (dto.questionType() != null) question.setQuestionType(dto.questionType());
        if (dto.points() != null) question.setPoints(dto.points());
        if (dto.difficulty() != null) question.setDifficulty(dto.difficulty());
        if (dto.answers() != null) {
            List<Long> incomingIds = dto.answers().stream()
                    .map(a -> a.id())
                    .filter(ansId -> ansId != null)
                    .toList();
            question.getAnswers().removeIf(existing -> !incomingIds.contains(existing.getId()));
            // Update existing or add new
            dto.answers().forEach(aDto -> {
                if (aDto.id() != null) {
                    // Update existing answer
                    Answer existingAnswer = question.getAnswers().stream()
                            .filter(a -> a.getId().equals(aDto.id()))
                            .findFirst()
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Answer " + aDto.id() + " not found in this question"));
                    existingAnswer.setText(aDto.text());
                    existingAnswer.setCorrect(aDto.correct());
                } else {
                    // Add new answer
                    Answer newAns = new Answer();
                    newAns.setText(aDto.text());
                    newAns.setCorrect(aDto.correct());
                    newAns.setQuestion(question); // Set relationship
                    question.getAnswers().add(newAns);
                }
            });
        }
        if (question.getQuestionType() == QuestionType.SINGLE_CHOICE ||
                question.getQuestionType() == QuestionType.TRUE_FALSE) {

            long correctCount = question.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .count();

            if (correctCount != 1) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Only 1 correct answer is allowed for this question type"
                );
            }
        }
        Question saved = questionRepository.save(question);
        return questionMapper.mapToResponse(saved);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Question Not Found!"));
        log.info("Deleting question with id: {}", question.getId());
        log.info("Question text: {}", question.getText());
        Quiz quiz = question.getQuiz();
        if (quiz != null) {
            quiz.getQuestions().remove(question);
        }
        questionRepository.delete(question);
        log.info("Question deleted successfully");
    }
}
