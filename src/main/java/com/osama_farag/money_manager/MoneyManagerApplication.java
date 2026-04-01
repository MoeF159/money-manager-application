package com.osama_farag.money_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MoneyManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyManagerApplication.class, args);
	}

}
// Press Ctrl+Shift+Y to open the Spring Boot dashboard and see the application running. 
// You can also run this class directly from your IDE to start the application.
