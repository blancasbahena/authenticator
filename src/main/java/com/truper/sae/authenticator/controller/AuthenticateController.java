package com.truper.sae.authenticator.controller;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.truper.ActiveDirectoryConnector;
import com.truper.sae.authenticator.configuration.JWUtil;
import com.truper.sae.authenticator.configuration.UserDetailsServices;
import com.truper.sae.commons.dto.AuthenticationRequest;
import com.truper.sae.commons.dto.AuthenticationResponse;
import com.truper.sae.commons.dto.ResponseVO;
import com.truper.sae.commons.dto.UserDTO;
import com.truper.sae.commons.enums.Mensajes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/authenticate")
@Slf4j
public class AuthenticateController {
	private final UserDetailsServices userDetailsServices;
	private final JWUtil jwutil;
	private final AuthenticationManager authenticationManager;
	private ActiveDirectoryConnector AD_CON = ActiveDirectoryConnector.getInstance();
	@PostMapping
	public ResponseEntity<?> createAuthenticationToken(@RequestBody
			AuthenticationRequest authenticationRequest) throws Exception {
		ResponseVO responseVO = ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje("Problems with user in Data Base")
				.build();
		try{	
			final UserDetails userDetails = userDetailsServices
					.loadUserByUsername(authenticationRequest.getUsername());
			AD_CON.validaLogin(authenticationRequest.getUsername(),authenticationRequest.getPassword());
			authenticate(authenticationRequest.getUsername(),authenticationRequest.getPassword());
			UserDTO dto=userDetailsServices.loadUserByUsernamePlusProfile(authenticationRequest.getUsername());
			if(dto!=null) {
				return ResponseEntity.ok(AuthenticationResponse.builder()
					.jwt(jwutil.generaToken(userDetails,dto))
					.build());
			}
		}catch(Exception e) {
			log.error("Problems with Authentication: {} ",e.getMessage());
			responseVO.setMensaje(e.getMessage());
		}
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