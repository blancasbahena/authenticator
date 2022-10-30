package com.truper.saen.authenticator.controller;
import javax.servlet.ServletRequest;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truper.sae.commons.dto.ResponseVO;
import com.truper.sae.commons.enums.Mensajes;
import com.truper.saen.authenticator.configuration.JWUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@RestController
@RequestMapping
@Slf4j
public class DecipherController {
	
	private final JWUtil jwutil;
	@RequestMapping(value = "/decipher", method = RequestMethod.GET)
	@ApiOperation(value = "Url que pide authenticacion con JWT", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> decipherToken(ServletRequest request, @RequestHeader("Authorization") String authorization) 
	{
		String mensajeError = "Problem with JWT";
		try
		{
			if (authorization != null && authorization.startsWith("Bearer ")) {
				authorization = authorization.substring(7);
			}
			if(jwutil.validateToken(authorization)) {
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

}