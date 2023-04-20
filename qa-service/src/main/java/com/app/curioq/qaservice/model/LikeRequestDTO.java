package com.app.curioq.qaservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequestDTO {
    private long userId;
    private long subjectId;
    private SubjectEnum type;
}
