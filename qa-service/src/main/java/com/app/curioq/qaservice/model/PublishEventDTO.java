package com.app.curioq.qaservice.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PublishEventDTO {
    private long publishedEntityId;
    private EventTypeEnum eventType;
    private String message;
    private LocalDateTime publishedAt;
}
