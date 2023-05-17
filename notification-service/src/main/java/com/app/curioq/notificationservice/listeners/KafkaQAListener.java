package com.app.curioq.notificationservice.listeners;

import com.app.curioq.notificationservice.model.PublishEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaQAListener {

    @KafkaListener(topics = "qa-topic",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(PublishEventDTO publishEventDTO) {
        log.info("KafkaQAListener ::: Received message - {}", publishEventDTO);
    }
}
