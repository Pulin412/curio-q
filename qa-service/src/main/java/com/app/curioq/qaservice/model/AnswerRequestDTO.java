package com.app.curioq.qaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerRequestDTO {

    private Long questionId;
    private String answer;
    private String email;
}
