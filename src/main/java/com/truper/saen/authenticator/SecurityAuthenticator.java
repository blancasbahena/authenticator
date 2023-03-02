package com.truper.saen.authenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@EnableEurekaClient
@Slf4j
@EnableFeignClients
@EntityScan("com.truper.saen.commons.entities")
public class SecurityAuthenticator {

	public static void main(String[] args) {
		BCryptPasswordEncoder dr= new BCryptPasswordEncoder();
		log.info("SALIDA:: "+dr.encode("321321"));
		SpringApplication.run(SecurityAuthenticator.class, args);
	}

}
