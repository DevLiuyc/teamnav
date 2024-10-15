package com.lyc.teamnav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TeamNavApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamNavApplication.class, args);
	}

}
