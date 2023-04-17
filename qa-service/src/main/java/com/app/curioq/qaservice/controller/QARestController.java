package com.app.curioq.qaservice.controller;

import com.app.curioq.qaservice.model.AnswerRequestDTO;
import com.app.curioq.qaservice.model.QAResponseDTO;
import com.app.curioq.qaservice.model.QuestionRequestDTO;
import com.app.curioq.qaservice.service.QAServiceImpl;
import com.app.curioq.qaservice.service.ValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class QARestController {

    private final QAServiceImpl qaService;
    private final ValidationService validationService;

    public QARestController(QAServiceImpl qaService, ValidationService validationService) {
        this.qaService = qaService;
        this.validationService = validationService;
    }

    @PostMapping("/question")
    public ResponseEntity<QAResponseDTO> submitQuestion(@RequestBody QuestionRequestDTO questionRequestDTO,
                                                        @RequestHeader String jwtToken){
        validationService.validateQuestionRequest(questionRequestDTO, jwtToken);
        return ResponseEntity.ok(qaService.submitQuestion(questionRequestDTO, jwtToken));
    }

    @PostMapping("/answer")
    public ResponseEntity<QAResponseDTO> submitAnswer(@RequestBody AnswerRequestDTO answerRequestDTO,
                                                      @RequestHeader String jwtToken){
        validationService.validateAnswerRequest(answerRequestDTO, jwtToken);
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
