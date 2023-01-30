package com.truper.saen.authenticator.service;

import java.util.List;

import com.truper.saen.authenticator.projection.PermisoProjection;
import com.truper.saen.commons.dto.MenuDTO;
import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.UserDTO;

public interface PermisosService {
	Boolean save(PermisoDTO dto);
	Boolean update(PermisoDTO dto);
	Boolean delete(PermisoDTO dto);
	List<PermisoDTO> findByRole(Long idRol);
	List<PermisoDTO> findByRoleUnassigned(Long idRol);
	PermisoDTO findById(Long idRol);
	List<PermisoDTO> findAll();
	PermisoDTO findByNombre(String nombrePermiso);
	Boolean appendPermissionToRol(Long idRol, Long idPermission,UserDTO userDTO);
	Boolean removePermissionToRol(Long idRol, Long idPermission,UserDTO userDTO);
	List<MenuDTO> findPermisosMenu(Long idUser);
	List<MenuDTO> findPantallasMenu(Long idRol);
	List<MenuDTO> findUnassing(Long idRol,Long idPantalla);
	List<MenuDTO> findAssing(Long idRol,Long idPantalla);
}
