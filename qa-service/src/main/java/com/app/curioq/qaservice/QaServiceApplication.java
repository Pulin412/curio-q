package com.app.curioq.qaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QaServiceApplication.class, args);
	}

}
