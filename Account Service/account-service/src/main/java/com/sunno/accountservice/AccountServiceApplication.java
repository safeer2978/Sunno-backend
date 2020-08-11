package com.sunno.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/*@SpringBootApplication(scanBasePackages={"com.sunno.accountservice","models", "controller", "com.sunno.accountservice.security","repository"})

@EntityScan("com.sunno.accountservice.models.persistence")*/
@EnableEurekaClient
@SpringBootApplication
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
