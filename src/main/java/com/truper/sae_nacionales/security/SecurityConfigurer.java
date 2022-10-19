package com.truper.sae_nacionales.security;

import org.springframework.context.annotation.Bean;
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

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfigurer  extends WebSecurityConfigurerAdapter {

	private final JwtRequestFilter jwtRequestFilter;
	private final UserDetailsServices myUserDatails;
			
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(myUserDatails);
	}  
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf()
			.disable()
			.authorizeRequests()
			.antMatchers("/v1/creaTokenAPP").permitAll()
			.antMatchers("/v1/swagger-resources/**").permitAll()
			.antMatchers("/v1/swagger-ui.html").permitAll()
			.antMatchers("/v1/actuator/info").permitAll()
			.antMatchers("/v1/swagger-ui/index.html").permitAll()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.anyRequest().authenticated()
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		
	}
	@Override
    public void configure(WebSecurity web) throws Exception 
    {
		web.ignoring().antMatchers(HttpMethod.POST, "/creaTokenAPP")
		.antMatchers("/api/v1/v2/api-docs/**")
		.antMatchers("/api/v1/swagger.json")
		.antMatchers("/api/v1/swagger-ui.html")
		.antMatchers("/api/v1/swagger-resources/**")
		.antMatchers("/api/v1/webjars/**"); 
    }
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	
	
	
}
