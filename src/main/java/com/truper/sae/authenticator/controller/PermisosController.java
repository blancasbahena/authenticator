package com.truper.sae.authenticator.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.truper.sae.authenticator.service.PermisosService;
import com.truper.sae.commons.dto.ResponseVO;
import com.truper.sae.commons.dto.PermisoDTO;
import com.truper.sae.commons.enums.Mensajes;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/permisos")
@Slf4j
public class PermisosController {
	@Autowired
	private PermisosService service;
	
	@GetMapping
	@ApiOperation(value = "Url que pide authenticacion con JWT", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getUsuarios(@RequestParam(required=false) Long idRol,@RequestParam(required=false) Long idPermiso){
		String mensaje="Problems in PermisosController @ GetMapping";
		try
		{
			Map<String, Object> formData = new HashMap<>();
			boolean exito=false;
			if(idRol!=null) {
				List<PermisoDTO> roles= service.findByRole(idRol);
				if(roles!=null && !roles.isEmpty()) {
					formData.put("permisos", roles);
					exito=true;
				}
			}
			else if(idPermiso!=null) {
				PermisoDTO rol= service.findById(idPermiso);
				if(rol!=null) {
					formData.put("permiso", rol);
					exito=true;
				}
			}
			else if(idPermiso==null && idRol==null) {
				List<PermisoDTO> roles=service.findAll();
				if(roles!=null && !roles.isEmpty()) {
					formData.put("permisos", roles);
					exito=true;
				}
			}
			else {
				return ResponseEntity.ok(ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
						.mensaje(Mensajes.MSG_NODATA.getMensaje())
						.build());
			}
			if(exito) {
				return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.build());
			}
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		return ResponseEntity.ok(ResponseVO.builder()
			.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
			.mensaje(mensaje)
			.build());
	}

	@PostMapping
	@ApiOperation(value = "Url que pide authenticacion con JWT", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> saveUsuarios(@RequestBody PermisoDTO dto){
		String mensaje="Problems in PermisosController @ PostMapping";
		Map<String, Object> formData = new HashMap<>();
		try
		{
			formData.put("result", service.save(dto));
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.build());
	}
	@PutMapping 
	@ApiOperation(value = "Url que pide authenticacion con JWT", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> modifyUsuarios(@RequestBody PermisoDTO dto){
		String mensaje="Problems in PermisosController @ PutMapping";
		Map<String, Object> formData = new HashMap<>();
		try
		{
			formData.put("result", service.update(dto));
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.build());
	} 
	
	@DeleteMapping 
	@ApiOperation(value = "Url que pide authenticacion con JWT", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> deleteUsuarios(@RequestBody PermisoDTO dto){
		String mensaje="Problems in PermisosController @ DeleteMapping";
		Map<String, Object> formData = new HashMap<>();
		try
		{
			formData.put("result", service.delete(dto));
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.build());
	}
}
