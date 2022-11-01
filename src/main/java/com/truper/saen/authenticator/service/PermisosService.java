package com.truper.saen.authenticator.service;

import java.util.List;

import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.UserDTO;

public interface PermisosService {
	Boolean save(PermisoDTO dto);
	Boolean update(PermisoDTO dto);
	Boolean delete(PermisoDTO dto);
	List<PermisoDTO> findByRole(Long idRol);
	PermisoDTO findById(Long idRol);
	List<PermisoDTO> findAll();
	PermisoDTO findByNombre(String nombrePermiso);
	Boolean appendPermissionToRol(Long idRol, Long idPermission,UserDTO userDTO);
	Boolean removePermissionToRol(Long idRol, Long idPermission,UserDTO userDTO);
}
