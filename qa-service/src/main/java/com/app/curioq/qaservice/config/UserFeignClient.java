package com.app.curioq.qaservice.config;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserFeignClient {
}
