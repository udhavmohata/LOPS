package com.hackathon.Lops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableMongoRepositories
@ComponentScan
@EnableScheduling
public class LopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LopsApplication.class, args);
	}

}
