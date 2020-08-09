package com.sunno.playservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PlayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayServiceApplication.class, args);
	}

}
