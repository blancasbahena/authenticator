package com.truper.saen.authenticator.configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.truper.sae.commons.dto.PermisoDTO;
import com.truper.sae.commons.dto.TokenPermisoDTO;
import com.truper.sae.commons.dto.TokenRoleDTO;
import com.truper.sae.commons.dto.UserDTO;

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

	public static String generaToken(UserDetails userDatails,UserDTO dto) {
		boolean existenRoles=false;
		List<TokenRoleDTO> roles=null;
		if(dto.getRoles()!=null ) {
			if(!dto.getRoles().isEmpty()) {
				existenRoles=true;
				roles =
				dto.getRoles().stream().map(m->
				   new TokenRoleDTO(
						   m.getDescripcion()!=null?m.getDescripcion():"",
								   m.getPermisos().stream().map(p->modelTokenPermiso(p)).collect(Collectors.toList()))
							).collect(Collectors.toList());
				existenRoles=true;
			}
		}
		Map<String, Object> claims = new HashMap<>();
	    claims.put("id", dto.getId());
	    claims.put("email", dto.getEmail());
	    claims.put("name", dto.getName());
	    if(existenRoles) {
	    	claims.put("roles",roles);
	    }
		return createToken(claims, userDatails.getUsername());
	}
	private static TokenPermisoDTO modelTokenPermiso(PermisoDTO dto) {
		if(dto!=null){
			return new TokenPermisoDTO(
				dto.getDescripcion()!=null?dto.getDescripcion():"",
				dto.getNombrePermiso()!=null?dto.getNombrePermiso():"",
				modelTokenPermiso(dto.getParent()),
				dto.getTipo()!=null?dto.getTipo():"",
				dto.getUrl()!=null?dto.getUrl():"",
				dto.getIcon()!=null?dto.getIcon():"",
				dto.getIdentifierAccion()!=null?dto.getIdentifierAccion():"",
				dto.getTooltip()!=null?dto.getTooltip():""
				);
		}
		return null;
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
