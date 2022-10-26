package com.truper.saen.configuration;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWUtil {

	private static String SECRET_KEY = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

	public static String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public static Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extraxtAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private static Claims extraxtAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private static Boolean istokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public static String generaToken(UserDetails userDatails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDatails.getUsername());
	}

	public static String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 150)))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public static Boolean validateToken(String token) {
		final String userName = extractUsername(token);
		return (!istokenExpired(token));
	}

}
