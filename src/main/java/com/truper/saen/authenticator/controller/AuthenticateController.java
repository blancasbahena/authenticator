package com.truper.saen.authenticator.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Base64;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.truper.ActiveDirectoryConnector;
import com.truper.saen.authenticator.configuration.JWUtil;
import com.truper.saen.authenticator.configuration.UserDetailsServices;
import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.commons.dto.AuthenticationRequest;
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
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
@Slf4j
public class AuthenticateController {
	private final UserDetailsServices userDetailsServices;
	private final JWUtil jwutil;
	private final UserService userService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final AuthenticationManager authenticationManager;
	private ActiveDirectoryConnector AD_CON = ActiveDirectoryConnector.getInstance();
	@PostMapping("/sigin")
	@ApiOperation(value = "Servicio para la auntenticacion con Body{ username / password } y regresa token")
	public ResponseEntity<?> createAuthenticationTokenAD(@RequestBody
			AuthenticationRequest authenticationRequest) throws Exception {
		ResponseVO responseVO = ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje("Problems with user in Data Base")
				.folio(ResponseVO.getFolioActual())
				.build();
		try{
			authenticationRequest = decodeBase64(authenticationRequest);
			log.info("Inicia proceso para authenticaion {} , {} ",authenticationRequest.getUsername(),Fechas.getHoraLogeo());
			UserDTO dto=userService.findByUserName(authenticationRequest.getUsername());
			if(dto!=null) {
				if(dto.getUserAD()) { 
					AD_CON.obtenBeanUsuario(authenticationRequest.getUsername(),authenticationRequest.getPassword());
				} // APP o  Proveedor
				final UserDetails userDetails = userDetailsServices
						.loadUserByUsername(authenticationRequest.getUsername());
				authenticate(authenticationRequest.getUsername(),authenticationRequest.getPassword());
				Map<String, Object> formData = new HashMap<>();
				formData.put("jwt",  jwutil.generaToken(userDetails,dto));
				log.info("Termina proceso para authenticaion   {} ", Fechas.getHoraLogeo());
				ResponseVO responseOK = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
						.mensaje(Mensajes.MSG_EXITO.getMensaje())
						.data(formData)
						.folio(ResponseVO.getFolioActual())
						.build();
				return ResponseEntity.status(HttpStatus.SC_OK).body(responseOK);
			}else {
				responseVO = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
						.mensaje("Problems with Authentication")
						.folio(ResponseVO.getFolioActual())
						.build();
			}
		}catch(Exception e) {
			log.error("Problems with Authentication: {} ",e.getMessage());
			responseVO.setMensaje("Problems with Authentication");
			responseVO.setFolio(ResponseVO.getFolioActual());
		}
		log.info("Termina proceso para authenticaion con error  {} ", Fechas.getHoraLogeo());
		return ResponseEntity.status(HttpStatus.SC_OK).body(responseVO);		
	}
	@PutMapping("/prepareReset")
	@ApiOperation(value = "Servicio para la auntenticacion con Body{ username / password } y regresa token")
	public ResponseEntity<?> prepareReset(@RequestBody
			AuthenticationRequest authenticationRequest) throws Exception {
		ResponseVO responseVO = ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje("Problems with user in Data Base")
				.folio(ResponseVO.getFolioActual())
				.build();
		try{
			authenticationRequest = decodeBase64(authenticationRequest);
			log.info("Inicia proceso para prepareReset {} , {} ",authenticationRequest.getUsername(),Fechas.getHoraLogeo());
			UserDTO dto=userService.prepareReset(authenticationRequest.getUsername());
			if(dto!=null) {
				Map<String, Object> formData = new HashMap<>();
				formData.put("reset", dto);
				log.info("Termina proceso para prepareReset   {} ", Fechas.getHoraLogeo());
				ResponseVO responseOK = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
						.mensaje(Mensajes.MSG_EXITO.getMensaje())
						.data(formData)
						.folio(ResponseVO.getFolioActual())
						.build();
				return ResponseEntity.status(HttpStatus.SC_OK).body(responseOK);
			}else {
				responseVO = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
						.mensaje("Problems with prepareReset")
						.folio(ResponseVO.getFolioActual())
						.build();
			}
		}catch(Exception e) {
			log.error("Problems with prepareReset: {} ",e.getMessage());
			responseVO.setMensaje("Problems with Authentication");
			responseVO.setFolio(ResponseVO.getFolioActual());
		}
		log.info("Termina proceso para authenticaion con error  {} ", Fechas.getHoraLogeo());
		return ResponseEntity.status(HttpStatus.SC_OK).body(responseVO);		
	}
	@PutMapping("/reset")
	@ApiOperation(value = "Servicio para la auntenticacion con Body{ username / password } y regresa token")
	public ResponseEntity<?> reset(@RequestBody
			AuthenticationRequest authenticationRequest) throws Exception {
		ResponseVO responseVO = ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje("Problems with user in Data Base")
				.folio(ResponseVO.getFolioActual())
				.build();
		try{
			authenticationRequest = decodeBase64(authenticationRequest);
			log.info("Inicia proceso para prepareReset {} , {} ",authenticationRequest.getUsername(),Fechas.getHoraLogeo());
			UserDTO dto=userService.reset(authenticationRequest.getUsername(), bCryptPasswordEncoder.encode(authenticationRequest.getPassword()));
			if(dto!=null) {
				Map<String, Object> formData = new HashMap<>();
				formData.put("reset", dto);
				log.info("Termina proceso para prepareReset   {} ", Fechas.getHoraLogeo());
				ResponseVO responseOK = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
						.mensaje(Mensajes.MSG_EXITO.getMensaje())
						.data(formData)
						.folio(ResponseVO.getFolioActual())
						.build();
				return ResponseEntity.status(HttpStatus.SC_OK).body(responseOK);
			}else {
				responseVO = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
						.mensaje("Problems with prepareReset")
						.folio(ResponseVO.getFolioActual())
						.build();
			}
		}catch(Exception e) {
			log.error("Problems with prepareReset: {} ",e.getMessage());
			responseVO.setMensaje("Problems with Authentication");
			responseVO.setFolio(ResponseVO.getFolioActual());
		}
		log.info("Termina proceso para authenticaion con error  {} ", Fechas.getHoraLogeo());
		return ResponseEntity.status(HttpStatus.SC_OK).body(responseVO);		
	} 
	@GetMapping("/read")
	@ApiOperation(value = "Servicio para validar si existe usuario y tenga correo")
	public ResponseEntity<?> getUser(@RequestBody
			AuthenticationRequest authenticationRequest) throws Exception {
		ResponseVO responseVO = ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje("Problems with user in Data Base")
				.folio(ResponseVO.getFolioActual())
				.build();
		try{ 
			log.info("Inicia proceso para authenticaion {} , {} ",authenticationRequest.getUsername(),Fechas.getHoraLogeo());
			UserDTO dto=userService.findByUserName(new String(Base64.getDecoder().decode(authenticationRequest.getUsername())));
			if(dto!=null) {
				Map<String, Object> formData = new HashMap<>();
				dto.setPassword(null);
				dto.setRoles(null);
				dto.setUserCreated(null);
				dto.setUserModified(null);
				dto.setCreated(null);
				dto.setModified(null);
				formData.put("user",  dto);
				log.info("Termina proceso para authenticaion   {} ", Fechas.getHoraLogeo());
				ResponseVO responseOK = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
						.mensaje(Mensajes.MSG_EXITO.getMensaje())
						.data(formData)
						.folio(ResponseVO.getFolioActual())
						.build();
				return ResponseEntity.status(HttpStatus.SC_OK).body(responseOK);
			}else {
				responseVO = ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
						.mensaje("Problems with read to user")
						.folio(ResponseVO.getFolioActual())
						.build();
			}
		}catch(Exception e) {
			log.error("Problems with read to user: {} ",e.getMessage());
			responseVO.setMensaje("Problems with read to user");
			responseVO.setFolio(ResponseVO.getFolioActual());
		}
		log.info("Termina proceso para leer user con error  {} ", Fechas.getHoraLogeo());
		return ResponseEntity.status(HttpStatus.SC_OK).body(responseVO);		
	} 

	private AuthenticationRequest decodeBase64(AuthenticationRequest authenticationRequest) {
		if(authenticationRequest!=null) {
			if(authenticationRequest.getUsername()!=null && !authenticationRequest.getUsername().isEmpty()) {
				authenticationRequest.setUsername(new String(Base64.getDecoder().decode(authenticationRequest.getUsername())));
			}
			if(authenticationRequest.getPassword()!=null && !authenticationRequest.getPassword().isEmpty()) {
				authenticationRequest.setPassword(new String(Base64.getDecoder().decode(authenticationRequest.getPassword())));
			}
		}
		return authenticationRequest;
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