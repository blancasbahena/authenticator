package com.truper.saen.controller.test;

import javax.servlet.ServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.truper.sae.commons.dto.ResponseVO;
import com.truper.saen.enums.Mensajes;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@Controller
@RequestMapping("/test")
public class TestSecurityController {
	@RequestMapping(value = "/request", method = RequestMethod.GET)
	@ApiOperation(value = "Url de prueba validacion de token", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> decipherToken(ServletRequest request) 
	{

		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
				.mensaje(Mensajes.MSG_TOKEN_EXITO.getMensaje())
				.build());	
	}
}