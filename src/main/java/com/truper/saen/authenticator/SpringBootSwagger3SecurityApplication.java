package com.truper.saen.authenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@EnableEurekaClient
@EntityScan("com.truper.saen.commons.entities")
public class SpringBootSwagger3SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSwagger3SecurityApplication.class, args);
	}

}