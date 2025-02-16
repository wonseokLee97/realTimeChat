package com.dev.realtimechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableMongoAuditing
@ComponentScan(basePackages = "com.dev")
public class RealTimeChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeChatApplication.class, args);
	}

}
