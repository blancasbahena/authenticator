package com.truper.saen.authenticator.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.truper.saen.authenticator.configuration.JWUtil;
import com.truper.saen.commons.dto.UserDTO; 

@Service
public class ValidaTokenServiceImpl implements ValidaTokenService{

	public UserDTO regresaUsuario(UserService service,JWUtil jwutil,HttpServletRequest request) throws Exception {
		UserDTO dto = null;
		String authorization = request.getHeader("authorization");
		if(authorization==null){
			throw new Exception("Error - Problems with user in Token");
		} 
		if (authorization != null && authorization.startsWith("Bearer ")) {
			authorization = authorization.substring(7);
		}
		if(!jwutil.validateToken(authorization)) { 
			throw new Exception("Error - Problems with Token");
		}
		String userName=jwutil.extractUsername(authorization);
		dto =  service.findByUserName(userName);
		if(dto==null) {
			throw new Exception("Error - Problems with user in Token");
		}
		return dto;
	}

}
