package com.truper.saen.authenticator.service;

import com.truper.sae.commons.dto.UserDTO;

public interface UserService {

	Boolean save(UserDTO dto);
	Boolean update(UserDTO dto);
	Boolean delete(UserDTO dto);
	UserDTO findById(Long idUser);
	UserDTO findByUserName(String userName);
}
