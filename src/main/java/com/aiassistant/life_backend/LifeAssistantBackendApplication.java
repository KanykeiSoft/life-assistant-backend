package com.aiassistant.life_backend;

import com.aiassistant.life_backend.ai.config.GeminiProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan
public class LifeAssistantBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifeAssistantBackendApplication.class, args);
	}

}
