package com.truper.saen.authenticator.service;

import java.util.List;

import com.truper.sae.commons.dto.PermisoDTO;

public interface PermisosService {
	Boolean save(PermisoDTO dto);
	Boolean update(PermisoDTO dto);
	Boolean delete(PermisoDTO dto);
	List<PermisoDTO> findByRole(Long idRol);
	PermisoDTO findById(Long idRol);
	List<PermisoDTO> findAll();
}
