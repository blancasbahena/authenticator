package com.truper.sae.authenticator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.truper.sae.authenticator.configuration.JWUtil;
import com.truper.sae.authenticator.configuration.UserDetailsServices;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class SpringBootSwagger3SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSwagger3SecurityApplication.class, args);
		UserDetailsServices service = new UserDetailsServices();
		UserDetails principal_2 = service.loadUserByUsername("example");
		JWUtil jwutil = new JWUtil();
		
		PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();	
		log.info("Este es un servicio de log4j {} ",jwutil.generaToken(principal_2,1l));
		log.debug("Este es un servicio de log4j {} ",jwutil.generaToken(principal_2,1l));
		log.warn("Este es un servicio de log4j {}",jwutil.generaToken(principal_2,1l));
		log.error("Este es un servicio de log4j {}",jwutil.generaToken(principal_2,1l));
	}

}