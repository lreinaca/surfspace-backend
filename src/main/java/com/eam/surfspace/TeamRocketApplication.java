package com.eam.surfspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TeamRocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamRocketApplication.class, args);
	}

}
