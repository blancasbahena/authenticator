package com.truper.saen.authenticator.service;

import java.util.List;
import java.util.Optional;

import com.truper.saen.authenticator.entities.Role;
import com.truper.saen.authenticator.entities.User;
import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO; 

public interface RolService {
	Boolean save(RoleDTO dto) throws  Exception;
	Boolean update(RoleDTO dto) throws  Exception;
	Boolean updateDetail(RoleDTO dto) throws  Exception;
	Boolean delete(RoleDTO dto) throws  Exception;
	List<RoleDTO> findByUser(Long idUser);
	RoleDTO findById(Long idRol) ;
	List<RoleDTO> findAll();
	Boolean removeRoleToUser(Long idUser,Long idRol,UserDTO o) throws  Exception; 
	Boolean appendRoleToUser(Long idUser,Long idRol,UserDTO o) throws  Exception;
}
