package co.istad.y2.quizzy.service;

import co.istad.y2.quizzy.dto.answer.AnswerResponseDto;
import co.istad.y2.quizzy.dto.question.CreateQuestionDto;
import co.istad.y2.quizzy.dto.question.QuestionResponseDto;
import co.istad.y2.quizzy.dto.question.UpdateQuestionDto;
import co.istad.y2.quizzy.exception.question.QuestionNotFoundException;
import co.istad.y2.quizzy.model.*;
import co.istad.y2.quizzy.repository.AnswerRepository;
import co.istad.y2.quizzy.repository.QuestionRepository;
import co.istad.y2.quizzy.repository.QuizRepository;
import co.istad.y2.quizzy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository,
                               AnswerRepository answerRepository,
                               UserRepository userRepository, QuizRepository quizRepository){
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
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
        answers.forEach(a->a.setQuestion(question));
        question.setAnswers(answers);
        question.setQuiz(quiz);
        question.setQuestionType(createQuestionDto.questionType());
        question.setPoints(createQuestionDto.points());
        question.setDifficulty(createQuestionDto.difficulty());
        Question saved = questionRepository.save(question);
        List<AnswerResponseDto> answerResponseDtos = saved.getAnswers().stream()
                .map(a->new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect())).toList();

        return new QuestionResponseDto(
                saved.getId(),
                saved.getText(),
                saved.getQuiz().getId(),
                answerResponseDtos,
                saved.getQuestionType(),
                saved.getPoints(),
                saved.getDifficulty()
        );
    }

    @Override
    public List<QuestionResponseDto> getAllQuestions(){
        return questionRepository.findAll().stream().map(q->{
            List<AnswerResponseDto> answerResponseDtos = q.getAnswers().stream()
                    .map(a-> new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect()))
                    .toList();
            return new QuestionResponseDto(
                    q.getId(),
                    q.getText(),
                    q.getQuiz().getId(),
                    answerResponseDtos,
                    q.getQuestionType(),
                    q.getPoints(),
                    q.getDifficulty()
            );
        }).toList();
    }

//    @Override
//    public List<QuestionResponseDto> getQuestionsByCategory(Long categoryId){
//        return questionRepository.findByCategoryId(categoryId).stream().
//                map(q->{
//                    List<AnswerResponseDto> answerResponseDtos = q.getAnswers().stream()
//                            .map(a-> new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect()))
//                            .toList();
//                    return new QuestionResponseDto(q.getId(),q.getText(),q.getCreatedBy().getUsername(),answerResponseDtos);
//                }).toList();
//    }

    @Override
    public QuestionResponseDto updateQuestion(Long id, UpdateQuestionDto dto){

        Question question = questionRepository.findById(id).orElseThrow(
                ()->new RuntimeException("Question Not Found!"));

        if(dto.text()!=null){
            question.setText(dto.text());
        }

        List<Answer> updatedAnswers = dto.answers().stream()
                .map(aDto -> {
                    Answer ans;
                    if(aDto.id()!=null){
                        ans = answerRepository.findById(aDto.id()).orElseThrow(()->new RuntimeException("Answer Not Found!"));
                        ans.setText(aDto.text());
                        ans.setCorrect(aDto.correct());
                    }else {
                        ans = new Answer();
                        ans.setText(aDto.text());
                        ans.setCorrect(aDto.correct());
                        ans.setQuestion(question);
                    }
                    return ans;
                }).collect(Collectors.toList());

        //REMOVE answers not included
        question.getAnswers().removeIf(
                exist->updatedAnswers.stream().noneMatch(
                        a->a.getId()!=null && a.getId().equals(exist.getId())
                )
        );
        //ADD new answers + updated
        for (Answer ans : updatedAnswers) {
            if (ans.getId() == null) {
                question.getAnswers().add(ans);
            }
        }
        Question saved = questionRepository.save(question);
        List<AnswerResponseDto> answerResponseDtos = saved.getAnswers().stream()
                .map(a->new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect()))
                .toList();

        return new QuestionResponseDto(
                saved.getId(),
                saved.getText(),
                saved.getQuiz().getId(),
                answerResponseDtos,
                saved.getQuestionType(),
                saved.getPoints(),
                saved.getDifficulty()
        );
    }

    @Override
    public void deleteQuestion(Long id){
        Question question = questionRepository.findById(id).orElseThrow(()->new RuntimeException("Quesion Not Found!"));
        questionRepository.delete(question);
    }

//    @Override
//    public QuestionResponseDto getQuestionById(Long id) {
//        Question question = questionRepository.findById(id).orElseThrow(
//                ()-> new QuestionNotFoundException("Question with this id doesn't exist!")
//        );
//
//        List<AnswerResponseDto> answerResponseDtos = saved.getAnswers().stream()
//                .map(a->new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect())).toList();
//
//
//        List<AnswerResponseDto> answerResponseDtos = question.getAnswers().stream()
//                .map(a->new AnswerResponseDto(a.getId(),a.getText(),a.isCorrect())).toList();
//        return new QuestionResponseDto(question.getId(),
//                question.getText(),
//                question.getCreatedBy().getUsername(),
//                answerResponseDtos
//        );
//    }

}
