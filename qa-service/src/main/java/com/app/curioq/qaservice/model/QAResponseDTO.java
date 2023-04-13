package com.app.curioq.qaservice.model;

import com.app.curioq.qaservice.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QAResponseDTO {
    private String message;
    private List<Question> questionList;
}
