package com.app.curioq.qaservice.controller;

import com.app.curioq.qaservice.model.AnswerRequestDTO;
import com.app.curioq.qaservice.model.QuestionRequestDTO;
import com.app.curioq.qaservice.model.QAResponseDTO;
import com.app.curioq.qaservice.service.QAService;
import com.app.curioq.qaservice.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class QARestController {

    private final QAService qaService;
    private final ValidationService validationService;

    public QARestController(QAService qaService, ValidationService validationService) {
        this.qaService = qaService;
        this.validationService = validationService;
    }

    @PostMapping("/question")
    public ResponseEntity<QAResponseDTO> submitQuestion(@RequestBody QuestionRequestDTO questionRequestDTO){
        validationService.validateQuestionRequest(questionRequestDTO);
        return ResponseEntity.ok(qaService.submitQuestion(questionRequestDTO));
    }

    @PostMapping("/answer")
    public ResponseEntity<QAResponseDTO> submitAnswer(@RequestBody AnswerRequestDTO answerRequestDTO){
        validationService.validateAnswerRequest(answerRequestDTO);
        return ResponseEntity.ok(qaService.submitAnswer(answerRequestDTO));
    }

    @GetMapping("/questions")
    public ResponseEntity<QAResponseDTO> getAllQuestions() {
        return ResponseEntity.ok(
                QAResponseDTO.builder()
                        .questionList(qaService.findAllQuestions())
                        .build()
        );
    }
}
