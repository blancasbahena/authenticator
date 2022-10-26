package com.truper.saen.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityContextConfiguration extends WebSecurityConfigurerAdapter
{

	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		 http
		 	.csrf().disable()
			.authorizeRequests()
				.antMatchers("/api/v1/swagger-resources/**").permitAll()
				.antMatchers("/api/v1/swagger-ui.html").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.anyRequest().authenticated()
            .and()
	            .sessionManagement()
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	.and()
        		.exceptionHandling()
        	.and()
        		.addFilterBefore(jwtAuthrorizationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	 
	@Bean
	public JwtAuthorizationFilter jwtAuthrorizationFilter() 
	{
		return new JwtAuthorizationFilter();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception 
    {
		web.ignoring().antMatchers(HttpMethod.POST, "/api/v1/decipher")
		.antMatchers("/api/v1/v2/api-docs/**")
		.antMatchers("/api/v1/swagger.json")
		.antMatchers("/api/v1/swagger-ui.html")
		.antMatchers("/api/v1/swagger-resources/**")
		.antMatchers("/api/v1/webjars/**");
    }
}
