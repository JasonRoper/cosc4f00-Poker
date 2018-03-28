package com.pokerface.pokerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The Main method, this runs the Back End
 */
@SpringBootApplication
@EnableScheduling
public class PokerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PokerApiApplication.class, args);
	}
}
