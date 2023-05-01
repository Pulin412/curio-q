package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.entity.Answer;
import com.app.curioq.qaservice.entity.Question;
import com.app.curioq.qaservice.exceptions.GenericException;
import com.app.curioq.qaservice.gateway.UserGatewayService;
import com.app.curioq.qaservice.gateway.UserResponseDTO;
import com.app.curioq.qaservice.model.*;
import com.app.curioq.qaservice.repository.AnswerRepository;
import com.app.curioq.qaservice.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QAServiceImpl implements QAService{

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserGatewayService userGatewayService;
    private final PublishEventService publishEventService;

    public QAServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, UserGatewayService userGatewayService, PublishEventService publishEventService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userGatewayService = userGatewayService;
        this.publishEventService = publishEventService;
    }


    @Transactional
    public QAResponseDTO submitQuestion(QuestionRequestDTO questionRequestDTO, String jwtToken) {
        String userEmail = questionRequestDTO.getEmail();
        UserResponseDTO userResponseDTO;

        try {
            userResponseDTO = userGatewayService.fetchUserByEmail(userEmail, jwtToken);
        } catch (Exception e){
            throw new GenericException("Issue with connecting with User Service, details - " + e.getMessage());
        }

        Question question = Question.builder()
                .title(questionRequestDTO.getTitle())
                .questionDescription(questionRequestDTO.getDescription())
                .answers(Collections.EMPTY_LIST)
                .userEmail(userResponseDTO.getEmail())
                .userId(userResponseDTO.getUserId())
                .likedBy(Collections.EMPTY_SET)
                .build();

        Question questionFromDb = questionRepository.save(question);

        if(!publishEventService.publish(PublishEventDTO.builder()
                .eventType(EventTypeEnum.QUESTION_CREATED)
                .publishedAt(LocalDateTime.now())
                .publishedEntityId(question.getId())
                .message("Question submitted")
                .build())){
            log.error("Unable to publish event for {}", question.getQuestionDescription());
        }

        return QAResponseDTO.builder()
                .message("question submitted with ID - " + questionFromDb.getId())
                .build();
    }

    @Transactional
    public List<Question> findAllQuestions() {
        return questionRepository.findAll();
    }

    @Transactional
    public QAResponseDTO submitAnswer(AnswerRequestDTO answerRequestDTO, String jwtToken) {
        Question questionFromDb = questionRepository.findById(answerRequestDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        List<Answer> answers = questionFromDb.getAnswers();

        String userEmail = answerRequestDTO.getEmail();
        UserResponseDTO userResponseDTO;

        try {
            userResponseDTO = userGatewayService.fetchUserByEmail(userEmail, jwtToken);
        } catch (Exception e){
            throw new GenericException("Issue with connecting with User Service, details - " + e.getMessage());
        }

        Answer answer = Answer.builder()
                .question(questionFromDb)
                .answerDescription(answerRequestDTO.getAnswer())
                .userEmail(userResponseDTO.getEmail())
                .userId(userResponseDTO.getUserId())
                .likedBy(Collections.EMPTY_SET)
                .build();

        Answer answerFromDb = answerRepository.save(answer);

        answers.add(answerFromDb);
        questionFromDb.setAnswers(answers);
        questionRepository.save(questionFromDb);

        return QAResponseDTO.builder()
                .message("answer submitted with ID - " + answerFromDb.getId() + " for question with ID - " + questionFromDb.getId())
                .build();
    }

    @Override
    public QAResponseDTO likeSubject(LikeRequestDTO likeRequestDTO, String jwtToken) {
        Optional<Question> optionalQuestion = Optional.empty();
        Optional<Answer> optionalAnswer = Optional.empty();
        long userId = 0;
        String type = likeRequestDTO.getType().name();

        if(type.equals(SubjectEnum.QUESTION.name())){
            optionalQuestion = Optional.ofNullable(questionRepository.findById(likeRequestDTO.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Question not found")));
        } else {
            optionalAnswer = Optional.ofNullable(answerRepository.findById(likeRequestDTO.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Answer not found")));
        }

        UserResponseDTO userResponseDTO;
        try {
            userResponseDTO = userGatewayService.fetchUserById(likeRequestDTO.getUserId(), jwtToken);
        } catch (Exception e){
            throw new GenericException("Issue with connecting with User Service, details - " + e.getMessage());
        }

        if(userResponseDTO != null){
            userId = userResponseDTO.getUserId();
        } else {
            throw new GenericException("User not found");
        }

        if(optionalQuestion.isPresent()){

            Question questionFromDb = optionalQuestion.get();
            questionFromDb.getLikedBy().add(userId);
            questionRepository.save(questionFromDb);
            return QAResponseDTO.builder()
                    .message("Question with ID " + likeRequestDTO.getSubjectId() + " liked by user with ID " + userId)
                    .build();

        } else if(optionalAnswer.isPresent()) {

            Answer answerFromDb = optionalAnswer.get();
            answerFromDb.getLikedBy().add(userId);
            answerRepository.save(answerFromDb);
            return QAResponseDTO.builder()
                    .message("Answer with ID " + likeRequestDTO.getSubjectId() + " liked by user with ID " + userId)
                    .build();

        } else {
            throw new GenericException("Invalid Subject");
        }
    }
}
