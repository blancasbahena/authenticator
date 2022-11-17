package com.truper.saen.authenticator.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truper.saen.authenticator.configuration.JWUtil;
import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.commons.dto.ResponseVO;
import com.truper.saen.commons.dto.UserDTO;
import com.truper.saen.commons.enums.Mensajes;
import com.truper.saen.commons.utils.Fechas;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class DecipherController {
	private final UserService userService;
	private final JWUtil jwutil;
	@RequestMapping(value = "/decipher", method = RequestMethod.GET)
	@ApiOperation(value = "Servicio para decifrar el token y revisa si es valido", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> decipherToken(ServletRequest request, @RequestHeader("Authorization") String authorization) 
	{
		log.info("Inicia proceso para validacion del token {} , {} ",authorization,Fechas.getHoraLogeo());
		String mensajeError = "Problem with JWT";
		try
		{
			if (authorization != null && authorization.startsWith("Bearer ")) {
				authorization = authorization.substring(7);
			}
			if(jwutil.validateToken(authorization)) {
				Map<String, Object> formData = new HashMap<>();
				formData.put("response", jwutil.modelTokenPermiso(userService.findByUserName(jwutil.extractUsername(authorization))));
				log.info("Termina proceso para validacion del token {} ",Fechas.getHoraLogeo());
				return ResponseEntity.ok(ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
						.folio(ResponseVO.getFolioActual())
						.data(formData)
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
		.folio(ResponseVO.getFolioActual())
		.build();
		log.info("Termina proceso para validacion del token con error {} ",Fechas.getHoraLogeo());
		return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(responseVO);			
	}

}