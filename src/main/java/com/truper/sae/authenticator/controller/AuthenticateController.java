package com.truper.sae.authenticator.controller;
import java.util.Date;

import javax.servlet.ServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truper.ActiveDirectoryConnector;
import com.truper.sae.authenticator.configuration.JWUtil;
import com.truper.sae.authenticator.configuration.UserDetailsServices;
import com.truper.sae.commons.dto.AuthenticationRequest;
import com.truper.sae.commons.dto.AuthenticationResponse;
import com.truper.sae.commons.dto.ResponseVO;
import com.truper.sae.commons.enums.Mensajes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class AuthenticateController {
	private final UserDetailsServices userDetailsServices;
	private final JWUtil jwutil;
	private final AuthenticationManager authenticationManager;
	private ActiveDirectoryConnector AD_CON = ActiveDirectoryConnector.getInstance();
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody
			AuthenticationRequest authenticationRequest) throws Exception {
		try{
			authenticate(authenticationRequest.getUsername(),authenticationRequest.getPassword());
			final UserDetails userDetails = userDetailsServices
					.loadUserByUsername(authenticationRequest.getUsername());
			
//			List<Usuario> usuario=  repositoryUser.findAll(); 
			return ResponseEntity.ok(AuthenticationResponse.builder()
					.jwt(jwutil.generaToken(userDetails,1l))
					.build());
		}catch(Exception e) {
			ResponseVO responseVO = ResponseVO.builder()
			.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
			.mensaje(e.getMessage())
			.build();
			return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(responseVO);
		}		
	}
	@RequestMapping(value = "/decipher", method = RequestMethod.GET)
	public ResponseEntity<ResponseVO> decipherToken(ServletRequest request, @RequestHeader("Authorization") String authorization) 
	{
		String mensajeError = "Problem with JWT";
		try
		{
			if (authorization != null && authorization.startsWith("Bearer ")) {
				authorization = authorization.substring(7);
			}
			if(jwutil.validateToken(authorization)) {
				String username = jwutil.extractUsername(authorization);
				Date expiration= JWUtil.extractExpiration(authorization);
				return ResponseEntity.ok(ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
						.mensaje(Mensajes.MSG_TOKEN_EXITO.getMensaje())
						.build());
			}
		} 
		catch (Exception e)
		{
			log.error(e.getMessage());	
			mensajeError=e.getMessage();
		}
		ResponseVO responseVO = ResponseVO.builder()
		.tipoMensaje(mensajeError)
		.mensaje(mensajeError)
		.build();
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(responseVO);			
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}