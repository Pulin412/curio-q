package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.exceptions.InvalidRequestException;
import com.app.curioq.qaservice.model.AnswerRequestDTO;
import com.app.curioq.qaservice.model.QuestionRequestDTO;
import com.app.curioq.qaservice.utils.QAConstants;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validateQuestionRequest(QuestionRequestDTO questionRequestDTO, String jwtToken) {
        if (questionRequestDTO.getTitle() == null ||
                questionRequestDTO.getTitle().isEmpty() ||
                questionRequestDTO.getDescription() == null ||
                questionRequestDTO.getDescription().isEmpty()) {

            throw new InvalidRequestException(QAConstants.EXCEPTION_INVALID_REQUEST_MESSAGE);
        }
        validateToken(jwtToken);
    }

    private void validateToken(String jwtToken) {
        // TODO : add impl here to validate token
    }

    public void validateAnswerRequest(AnswerRequestDTO answerRequestDTO, String jwtToken) {
        if (answerRequestDTO.getAnswer() == null ||
                answerRequestDTO.getAnswer().isEmpty() ||
                answerRequestDTO.getQuestionId() == null) {

            throw new InvalidRequestException(QAConstants.EXCEPTION_INVALID_REQUEST_MESSAGE);
        }
        validateToken(jwtToken);
    }
}
