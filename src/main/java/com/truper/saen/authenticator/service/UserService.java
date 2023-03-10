package com.truper.saen.authenticator.service;

import java.util.List;

import com.truper.saen.authenticator.dto.ResetPasswordDTO;
import com.truper.saen.commons.dto.UserDTO;

public interface UserService {

	Boolean save(UserDTO dto) throws Exception;
	public Boolean update(UserDTO dto,boolean modificarRoles,boolean soloPassword) throws Exception ;
	Boolean delete(UserDTO dto) throws  Exception;
	UserDTO findById(Long idUser);
	List<UserDTO> findall();
	UserDTO findByUserName(String userName);
	ResetPasswordDTO prepareReset(String userName,String authorization) throws Exception;
	UserDTO reset(String userName,String password) throws  Exception;
}
