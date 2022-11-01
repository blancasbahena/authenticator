package com.truper.saen.authenticator.controller;
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
import com.truper.saen.authenticator.configuration.JWUtil;
import com.truper.saen.authenticator.configuration.UserDetailsServices;
import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.commons.dto.AuthenticationRequest;
import com.truper.saen.commons.dto.AuthenticationResponse;
import com.truper.saen.commons.dto.ResponseVO;
import com.truper.saen.commons.dto.UserDTO;
import com.truper.saen.commons.enums.Mensajes;
import com.truper.saen.commons.utils.Fechas;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/authenticate")
@Slf4j
public class AuthenticateController {
	private final UserDetailsServices userDetailsServices;
	private final JWUtil jwutil;
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private ActiveDirectoryConnector AD_CON = ActiveDirectoryConnector.getInstance();
	@PostMapping
	@ApiOperation(value = "Servicio para la auntenticacion de usuario / password y regresa token")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody
			AuthenticationRequest authenticationRequest) throws Exception {
		ResponseVO responseVO = ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje("Problems with user in Data Base")
				.folio(ResponseVO.getFolioActual())
				.build();
		try{
			log.info("Inicia proceso para authenticaion {} , {} ",authenticationRequest.getUsername(),Fechas.getHoraLogeo());
			final UserDetails userDetails = userDetailsServices
					.loadUserByUsername(authenticationRequest.getUsername());
			AD_CON.validaLogin(authenticationRequest.getUsername(),authenticationRequest.getPassword());
			authenticate(authenticationRequest.getUsername(),authenticationRequest.getPassword());
			UserDTO dto=userService.findByUserName(authenticationRequest.getUsername());
			if(dto!=null) {
				log.info("Termina proceso para authenticaion   {} ", Fechas.getHoraLogeo());
				return ResponseEntity.ok(AuthenticationResponse.builder()
						.folio(ResponseVO.getFolioActual())
						.jwt(jwutil.generaToken(userDetails,dto))
						.build());
			}
		}catch(Exception e) {
			log.error("Problems with Authentication: {} ",e.getMessage());
			responseVO.setMensaje(e.getMessage());
			responseVO.setFolio(ResponseVO.getFolioActual());
		}
		log.info("Termina proceso para authenticaion con error  {} ", Fechas.getHoraLogeo());
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(responseVO);		
	} 

	private void authenticate(String username, String password) throws Exception {
		log.info("Inicia proceso para authenticationManager {} , {} ",username,Fechas.getHoraLogeo());
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			log.info("Termina proceso para authenticationManager {} ",Fechas.getHoraLogeo());
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}