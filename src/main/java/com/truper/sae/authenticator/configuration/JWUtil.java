package com.truper.sae.authenticator.configuration;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.LinkedHashMap;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
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

	private static Boolean isTokenExpired(String token) {
		log.info("Token Expiration  is : {}",extractExpiration(token));
		log.info("Today             is : {}",new Date());
		log.info("Token Expiration  ?? : {}",extractExpiration(token).before(new Date()));
		return extractExpiration(token).before(new Date());
	}

	public static String generaToken(UserDetails userDatails,Long id) {
		Map<String, Object> claims = new HashMap<>();
	    claims.put("id", id);
	    claims.put("username", userDatails.getUsername());
	    claims.put("roles", Arrays.asList("ROL_USER","ROL_ADMIN"));
	    claims.put("permission", Arrays.asList("EDITAR","CONSULTAR","BORRAR","MODIFICAR"));
		return createToken(claims, userDatails.getUsername());
	} 
	public static String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 150)))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}
	//validate token
	public Boolean validateTokenAndUser(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	public static Boolean validateToken(String token) {
		final String userName = extractUsername(token);
		boolean expiredToken=isTokenExpired(token);
		return (!expiredToken);
	} 
}
