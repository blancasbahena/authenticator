package com.truper.sar_nacionales.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Restemplates {
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
