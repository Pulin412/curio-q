package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.entity.Answer;
import com.app.curioq.qaservice.entity.Question;
import com.app.curioq.qaservice.model.AnswerRequestDTO;
import com.app.curioq.qaservice.model.QuestionRequestDTO;
import com.app.curioq.qaservice.model.QAResponseDTO;
import com.app.curioq.qaservice.repository.AnswerRepository;
import com.app.curioq.qaservice.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QAService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QAService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public QAResponseDTO submitQuestion(QuestionRequestDTO questionRequestDTO) {
        Question question = questionRepository.save(mapQuestionModelToEntity(questionRequestDTO));
        return QAResponseDTO.builder()
                .message("question submitted with ID - " + question.getId())
                .build();
    }

    private Question mapQuestionModelToEntity(QuestionRequestDTO questionRequestDTO) {
        return Question.builder()
                .title(questionRequestDTO.getTitle())
                .questionDescription(questionRequestDTO.getDescription())
                .answers(Collections.EMPTY_LIST)
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
