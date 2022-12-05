package com.truper.saen.authenticator.configuration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServices myUserDatails;

	@Autowired
	private JWUtil jWUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizer = request.getHeader("Authorization");
		String username = null;
		String jwt = null;
		if (authorizer != null) {
			if (authorizer.equals("Bearer null")) {

			} else {
				if (authorizer.startsWith("Bearer ")) {
					jwt = authorizer.substring(7);
				}
				try {
					username = jWUtil.extractUsername(jwt);
					if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
						UserDetails userDetails = this.myUserDatails.loadUserByUsername(username);
						if (JWUtil.validateToken(jwt)) {
							UsernamePasswordAuthenticationToken userPassToken = new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
							userPassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
							SecurityContextHolder.getContext().setAuthentication(userPassToken);

						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		filterChain.doFilter(request, response);

	}

}