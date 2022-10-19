package com.truper.sae_nacionales.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter{

	private final UserDetailsServices myUserDatails;
	private final JWUtil jWUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final  String  authorizer =  request.getHeader("Authorization"); 
		String username =null;
		String jwt =null;
		if(authorizer!=null && authorizer.startsWith("Bearer ")) {
			jwt=  authorizer.substring(7);
			username =  jWUtil.extractUsername(jwt);
		}
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails  userDetails=  this.myUserDatails.loadUserByUsername(username);
			if(JWUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken userPassToken = 
						new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				userPassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(userPassToken);
				
			}
		}
		filterChain.doFilter(request, response);
	}
}
