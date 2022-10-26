package com.truper.saen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.userdetails.UserDetails;

import com.truper.saen.configuration.JWUtil;
import com.truper.saen.configuration.UserDetailsServices;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@Slf4j
public class SpringBootSwagger3SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootSwagger3SecurityApplication.class, args);
		UserDetailsServices service = new UserDetailsServices();
		UserDetails principal_2 = service.loadUserByUsername("example");
		JWUtil jwutil = new JWUtil();
		
		log.info("Este es un servicio de log4j {} ",jwutil.generaToken(principal_2));
		log.debug("Este es un servicio de log4j {} ",jwutil.generaToken(principal_2));
		log.warn("Este es un servicio de log4j {}",jwutil.generaToken(principal_2));
		log.error("Este es un servicio de log4j {}",jwutil.generaToken(principal_2));
	}

}