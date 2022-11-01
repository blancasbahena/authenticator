package com.truper.saen.authenticator.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.TokenDTO;
import com.truper.saen.commons.dto.TokenPermisoDTO;
import com.truper.saen.commons.dto.TokenRoleDTO;
import com.truper.saen.commons.dto.UserDTO;

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
	public static Integer extractId(String token) {
		Claims claims=extraxtAllClaims(token);
		return (Integer) claims.get("id");
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
		TokenDTO token=null;
		if(dto.getRoles()!=null ) {
			if(!dto.getRoles().isEmpty()) {
				existenRoles=true;
				token= modelTokenPermiso(dto.getRoles());
			}
		}
		Map<String, Object> claims = new HashMap<>();
	    claims.put("id", dto.getId());
	    claims.put("email", dto.getEmail());
	    claims.put("name", dto.getName());
	    if(existenRoles) {
	    	claims.put("token",token);
	    }
		return createToken(claims, userDatails.getUsername());
	}
	private static TokenDTO modelTokenPermiso(List<RoleDTO> rolesDTO) {
		
		List<TokenRoleDTO> roles =new ArrayList<TokenRoleDTO>();
		List<TokenPermisoDTO> permissions=  new ArrayList<TokenPermisoDTO>();
		rolesDTO.stream().forEach(r->{
				roles.add(new TokenRoleDTO(r.getId(),r.getDescripcion()));
				if(r.getPermisos()!=null)
				{
					permissions.addAll(
						r.getPermisos().stream().map(p->
						new TokenPermisoDTO(
								p.getId()!=null?p.getId():0l,
								p.getDescripcion()!=null?p.getDescripcion():"",
								p.getNombrePermiso()!=null?p.getNombrePermiso():"",
								p.getParent()!=null?p.getParent().getId():null,
								p.getTipo()!=null?p.getTipo():"",
								p.getUrl()!=null?p.getUrl():"",
								p.getIcon()!=null?p.getIcon():"",
								p.getIdentifierAccion()!=null?p.getIdentifierAccion():"",
								p.getTooltip()!=null?p.getTooltip():""
						)).collect(Collectors.toList()));
				}
			});
		return new TokenDTO(roles,permissions.stream().filter( distinctByKey(p -> p.getId()) ).collect( Collectors.toList()));
	}
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
	{
	    Map<Object, Boolean> map = new ConcurrentHashMap<>();
	    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	public static String createToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 150)))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	}

	public static Boolean validateToken(String token) {
		final String userName = extractUsername(token);
		Integer id =extractId(token);
		return (!istokenExpired(token));
	}

}
