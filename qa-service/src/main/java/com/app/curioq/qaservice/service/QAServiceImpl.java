package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.entity.Answer;
import com.app.curioq.qaservice.entity.Question;
import com.app.curioq.qaservice.exceptions.GenericException;
import com.app.curioq.qaservice.gateway.UserGatewayService;
import com.app.curioq.qaservice.gateway.UserResponseDTO;
import com.app.curioq.qaservice.model.AnswerRequestDTO;
import com.app.curioq.qaservice.model.QAResponseDTO;
import com.app.curioq.qaservice.model.QuestionRequestDTO;
import com.app.curioq.qaservice.repository.AnswerRepository;
import com.app.curioq.qaservice.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QAServiceImpl implements QAService{

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserGatewayService userGatewayService;

    public QAServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, UserGatewayService userGatewayService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userGatewayService = userGatewayService;
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
                .build();

        Question questionFromDb = questionRepository.save(question);
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
                .build();

        Answer answerFromDb = answerRepository.save(answer);

        answers.add(answerFromDb);
        questionFromDb.setAnswers(answers);
        questionRepository.save(questionFromDb);

        return QAResponseDTO.builder()
                .message("answer submitted with ID - " + answerFromDb.getId() + " for question with ID - " + questionFromDb.getId())
                .build();
    }
}
