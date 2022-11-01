package com.truper.saen.authenticator.service;

import java.util.List;
import java.util.Optional;

import com.truper.saen.authenticator.entities.Role;
import com.truper.saen.authenticator.entities.User;
import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO; 

public interface RolService {
	Boolean save(RoleDTO dto);
	Boolean update(RoleDTO dto);
	Boolean updateDetail(RoleDTO dto);
	Boolean delete(RoleDTO dto);
	List<RoleDTO> findByUser(Long idUser);
	RoleDTO findById(Long idRol);
	List<RoleDTO> findAll();
	Boolean removeRoleToUser(Long idUser,Long idRol,UserDTO o);
	Boolean appendRoleToUser(Long idUser,Long idRol,UserDTO o);
}
