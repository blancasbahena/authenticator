package com.truper.saen.authenticator.service;

import java.util.List;

import com.truper.sae.commons.dto.RoleDTO; 

public interface RolService {
	Boolean save(RoleDTO dto);
	Boolean update(RoleDTO dto);
	Boolean delete(RoleDTO dto);
	List<RoleDTO> findByUser(Long idUser);
	RoleDTO findById(Long idRol);
	List<RoleDTO> findAll();
}
