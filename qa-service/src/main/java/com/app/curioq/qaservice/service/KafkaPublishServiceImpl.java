package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.model.PublishEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaPublishServiceImpl implements PublishEventService{

    private final KafkaTemplate<String, PublishEventDTO> kafkaTemplate;

    @Value(value = "${kafka.topic.question.answer.submit}")
    private String qaSubmitTopicName;

    public KafkaPublishServiceImpl(KafkaTemplate<String, PublishEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean publish(PublishEventDTO publishEventDTO) {
        kafkaTemplate.send(qaSubmitTopicName, publishEventDTO);
        log.info("PUBLISH EVENT ::: Published event with details {}", publishEventDTO);
        return true;
    }
}
