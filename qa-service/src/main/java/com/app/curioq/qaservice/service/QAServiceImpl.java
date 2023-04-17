package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.entity.Answer;
import com.app.curioq.qaservice.entity.Question;
import com.app.curioq.qaservice.feign.UserResponseDTO;
import com.app.curioq.qaservice.gateway.QaGatewayService;
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
    private final QaGatewayService qaGatewayService;

    public QAServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, QaGatewayService qaGatewayService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.qaGatewayService = qaGatewayService;
    }


    @Transactional
    public QAResponseDTO submitQuestion(QuestionRequestDTO questionRequestDTO, String jwtToken) {
        String userEmail = questionRequestDTO.getEmail();
        String header = "Bearer " + jwtToken;
        UserResponseDTO userResponseDTO = qaGatewayService.fetchUserByEmail(userEmail, jwtToken);

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
    public QAResponseDTO submitAnswer(AnswerRequestDTO answerRequestDTO) {
        Question questionFromDb = questionRepository.findById(answerRequestDTO.getQuestionId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        List<Answer> answers = questionFromDb.getAnswers();
        Answer answer = Answer.builder()
                .question(questionFromDb)
                .answerDescription(answerRequestDTO.getAnswer())
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
