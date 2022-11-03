package com.truper.saen.authenticator.service;

import com.truper.saen.commons.dto.UserDTO;

public interface UserService {

	Boolean save(UserDTO dto) throws Exception;
	public Boolean update(UserDTO dto,boolean modificarRoles,boolean soloPassword) throws Exception ;
	Boolean delete(UserDTO dto) throws  Exception;
	UserDTO findById(Long idUser);
	UserDTO findByUserName(String userName);
}
