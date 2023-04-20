package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.entity.Question;
import com.app.curioq.qaservice.model.AnswerRequestDTO;
import com.app.curioq.qaservice.model.LikeRequestDTO;
import com.app.curioq.qaservice.model.QAResponseDTO;
import com.app.curioq.qaservice.model.QuestionRequestDTO;

import java.util.List;

public interface QAService {

    QAResponseDTO submitQuestion(QuestionRequestDTO questionRequestDTO, String jwtToken);
    List<Question> findAllQuestions();
    QAResponseDTO submitAnswer(AnswerRequestDTO answerRequestDTO, String jwtToken);
    QAResponseDTO likeSubject(LikeRequestDTO likeRequestDTO, String jwtToken);
}
