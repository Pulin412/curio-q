package com.app.curioq.qaservice.service;

import com.app.curioq.qaservice.model.PublishEventDTO;

public interface PublishEventService {
    boolean publish(PublishEventDTO publishEventDTO);
}
