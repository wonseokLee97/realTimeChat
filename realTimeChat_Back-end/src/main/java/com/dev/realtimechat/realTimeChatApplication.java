package com.dev.realtimechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class realTimeChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(realTimeChatApplication.class, args);
	}

}
