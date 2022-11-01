package com.truper.saen.authenticator.service;

import javax.servlet.http.HttpServletRequest;

import com.truper.saen.authenticator.configuration.JWUtil;
import com.truper.saen.commons.dto.UserDTO;

public interface ValidaTokenService {
	public UserDTO regresaUsuario(UserService service,JWUtil jwutil,HttpServletRequest request) throws Exception;
}
