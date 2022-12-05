package com.truper.saen.authenticator.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityContextConfiguration extends WebSecurityConfigurerAdapter
{
	@Autowired
	private UserDetailsServices userDetailsServices;
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		 http
		 	.csrf().disable()
			.authorizeRequests()
				.antMatchers("/api/v1/swagger-resources/**").permitAll()
				.antMatchers("api/v1/authenticate/**").permitAll()				
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
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServices).passwordEncoder(passwordEncoder());
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Override
    public void configure(WebSecurity web) throws Exception 
    {
		web.ignoring()
		.antMatchers(HttpMethod.POST, "/api/v1/authenticate/**")
		.antMatchers("/api/v1/v2/api-docs/**")
		.antMatchers("/api/v1/swagger.json")
		.antMatchers("/api/v1/swagger-ui.html")
		.antMatchers("/api/v1/swagger-resources/**")
		.antMatchers("/api/v1/webjars/**");
    }
}
