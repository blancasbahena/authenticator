package com.truper.saen.authenticator;
import org.apache.logging.log4j.core.util.PasswordDecryptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@EnableEurekaClient
@Slf4j
@EntityScan("com.truper.saen.commons.entities")
public class SpringBootSwagger3SecurityApplication {

	public static void main(String[] args) {
		BCryptPasswordEncoder dr= new BCryptPasswordEncoder();
		log.info("SALIDA:: "+dr.encode("321321"));
		SpringApplication.run(SpringBootSwagger3SecurityApplication.class, args);
	}

}