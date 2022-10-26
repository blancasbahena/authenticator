package com.truper.saen.controller.decipher;
import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.truper.sae.commons.dto.ResponseVO;
import com.truper.saen.configuration.JWUtil;
import com.truper.saen.configuration.UserDetailsServices;
import com.truper.saen.enums.Mensajes;

import lombok.extern.slf4j.Slf4j; 
@RestController
@RequestMapping
@Slf4j
public class DecipherController {   
	
	@Autowired
	private UserDetailsServices service;
	
	@Autowired
	private JWUtil jwutil;
	
	
	@RequestMapping(value = "/decipher", method = RequestMethod.POST)
	public ResponseEntity<ResponseVO> decipherToken(ServletRequest request, @RequestParam("token") String token) 
	{
		UserDetails principal = null;
		String mensajeError = null;
		UserDetails principal_2 = service.loadUserByUsername("example");
		System.out.print(jwutil.generaToken(principal_2));

		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
				.mensaje(Mensajes.MSG_TOKEN_EXITO.getMensaje())
				.build());	
	}

}