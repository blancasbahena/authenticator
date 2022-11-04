package com.truper.saen.authenticator.service;

import java.util.List;

import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO; 

public interface RolService {
	Boolean save(RoleDTO dto) throws  Exception;
	Boolean delete(RoleDTO dto) throws  Exception;
	List<RoleDTO> findByUser(Long idUser);
	List<RoleDTO> findByUserUnassigned (Long idUser);
	RoleDTO findById(Long idRol) ;
	List<RoleDTO> findAll();
	Boolean update(RoleDTO dto,boolean modifyPermission) throws  Exception;
	Boolean removeRoleToUser(Long idUser,Long idRol,UserDTO o) throws  Exception; 
	Boolean appendRoleToUser(Long idUser,Long idRol,UserDTO o) throws  Exception;
	RoleDTO findByDescripcion(String descripcion);
}
