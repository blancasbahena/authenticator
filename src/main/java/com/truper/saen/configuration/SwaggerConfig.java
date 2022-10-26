package com.truper.saen.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig 
{
	@Bean
    public Docket api() 
	{ 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("com.truper.saen.controller"))
          .paths(PathSelectors.any())  
          .build()
          .securitySchemes(Collections.singletonList(new ApiKey("Bearer", "Authorization", "header")))
          .apiInfo(apiInfo());                                       
    }
	
	private ApiInfo apiInfo() 
	{
		return new ApiInfoBuilder()
			.title("Archetype-Dechiper Documentation")
			.description("Archetype-Srping-Boot-Dechiper API´s Documentation for systems integrations through Truper")
			.version("1.0.0")
			.licenseUrl("LICENSE URL")
			.build();	
	}	
}
