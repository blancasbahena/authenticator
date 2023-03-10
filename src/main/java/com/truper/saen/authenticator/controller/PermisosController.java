package com.truper.saen.authenticator.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.truper.saen.authenticator.configuration.JWUtil;
import com.truper.saen.authenticator.service.PermisosService;
import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.authenticator.service.ValidaTokenService;
import com.truper.saen.commons.dto.MenuDTO;
import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.ResponseVO;
import com.truper.saen.commons.dto.UserDTO;
import com.truper.saen.commons.enums.Mensajes;
import com.truper.saen.commons.utils.Fechas;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/permissions")
@Slf4j
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class PermisosController {
	private final PermisosService service;
	private final JWUtil jwutil;
	private final ValidaTokenService tokenService;
	private final UserService serviceUser;
	@GetMapping
	@ApiOperation(value = "Servicio para consultar un permiso o varios, parametros{vacios, idPermiso o nombrePermiso}", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getUsuarios(@RequestParam(required=false) Long idRol,@RequestParam(required=false) Long idPermiso,
			@RequestParam(required=false) String nombrePermiso,@RequestHeader("Authorization") String authorization){
		String mensaje="Problems in PermisosController @ GetMapping";
		log.info("Inicia controller obtencion de permisos {}" ,Fechas.getHoraLogeo());
		log.info("Paramaetros {} , {} , {} ",
				idRol!=null?idRol:"-",
				idPermiso!=null?idPermiso:"-",
				nombrePermiso!=null?nombrePermiso:"-");
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
			else if(nombrePermiso!=null) {
				PermisoDTO rol= service.findByNombre(nombrePermiso);
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
				log.info("Termina controller de obtencion de permisos con error {}",Fechas.getHoraLogeo());
				return ResponseEntity.ok(ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
						.mensaje(Mensajes.MSG_NODATA.getMensaje())
						.folio(ResponseVO.getFolioActual())
						.build());
			}
			if(exito) {
				log.info("Termina controller de obtencion de permisos con exito {}",Fechas.getHoraLogeo());
				return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.data(formData)
					.build());
			}
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller de obtencion de permisos con error(2) {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
			.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
			.folio(ResponseVO.getFolioActual())
			.mensaje(mensaje)
			.build());
	}

	@PostMapping
	@ApiOperation(value = "Servicio para crear crear un permiso, parametro { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> saveUsuarios(@RequestBody PermisoDTO dto,HttpServletRequest request){
		log.info("Controller para guardar roles  {} ,{}",dto.toString(),Fechas.getHoraLogeo());
		String mensaje="Problems in PermisosController @ PostMapping";
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{
			usuarioToken = tokenService.regresaUsuario(serviceUser, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			dto.setUserCreated(usuarioToken);
			formData.put("result", service.save(dto));
			log.info("Termina controller para guardar roles con exito {} ",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para guardar permisos con error {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.folio(ResponseVO.getFolioActual())
				.build());
	}
	@PutMapping 
	@ApiOperation(value = "Servicio para crear modificar un permiso, parametro { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> modifyUsuarios(@RequestBody PermisoDTO dto,HttpServletRequest request){
		String mensaje="Problems in PermisosController @ PutMapping";
		log.info("Controller para modificar roles {}  {} ",dto.toString(),Fechas.getHoraLogeo());
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{
			usuarioToken = tokenService.regresaUsuario(serviceUser, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			dto.setUserModified(usuarioToken);
			formData.put("result", service.update(dto));
			log.info("Termina controller para modificar roles con exito {}",Fechas.getHoraLogeo() );
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para modificar permisos con error {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	} 
	
	@DeleteMapping 
	@ApiOperation(value = "Servicio para des-activar un permiso, parametro { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> deleteUsuarios(@RequestBody PermisoDTO dto,HttpServletRequest request){
		String mensaje="Problems in PermisosController @ DeleteMapping";
		log.info("Controller para borrar roles {} ,{}",dto.toString(),Fechas.getHoraLogeo());
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{
			usuarioToken = tokenService.regresaUsuario(serviceUser, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			dto.setUserModified(usuarioToken);
			formData.put("result", service.delete(dto));
			log.info("Termina controller para borrar roles con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para borrar permisos con error {} ",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	}
	@PatchMapping("/append")
	@ApiOperation(value = "Servicio para agregar un permiso a un rol, parametro { idRol y idPermission }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> appendPermissionToRol(@RequestParam(required=true) Long idRol,
			@RequestParam(required=true) Long idPermission,HttpServletRequest request){
		String mensaje="Problems in PermisosController @PatchMapping";
		log.info("Controller para asignar permiso a Rol {} ,{}",idRol,idPermission);
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{
			usuarioToken = tokenService.regresaUsuario(serviceUser, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			formData.put("result", service.appendPermissionToRol(idRol,idPermission,usuarioToken));
			log.info("Termina controller para borrar roles con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para borrar permisos con error {} ",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	}
	
	@PatchMapping("/appendList")
	@ApiOperation(value = "Servicio para agregar un permiso a un rol, parametro { idRol y idPermission }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> appendPermissionsToRol(@RequestParam(required=true) Long idRol,
			@RequestParam(required=true) List<Long> idPermissions,HttpServletRequest request){
		String mensaje="Problems in PermisosController @PatchMapping";
		log.info("Controller para asignar permiso a Rol {} ,{}",idRol,idPermissions);
		Map<String, Object> formData = new HashMap<>();
		try
		{
			tokenService.regresaUsuario(serviceUser, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			idPermissions.stream().forEach((idPermission)->{
				UserDTO usuarioToken =null;
				try
				{
					usuarioToken = tokenService.regresaUsuario(serviceUser, jwutil, request);
				} catch (Exception e) {
					log.error("Error: {}",e.getMessage());
				}
				formData.put("result", service.appendPermissionToRol(idRol,idPermission,usuarioToken));
			});
			
			log.info("Termina controller para borrar roles con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para borrar permisos con error {} ",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	}
	@PatchMapping("/remove")
	@ApiOperation(value = "Servicio para remover permiso a un rol, parametro { idRol y idPermission }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> removePermissionToRol(@RequestParam(required=true) Long idRol,
			@RequestParam(required=true) Long idPermission,HttpServletRequest request){
		String mensaje="Problems in PermisosController @PatchMapping";
		log.info("Controller para asignar permiso a Rol {} ,{}",idRol,idPermission);
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{
			usuarioToken = tokenService.regresaUsuario(serviceUser, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			
			formData.put("result", service.removePermissionToRol(idRol,idPermission,usuarioToken));
			log.info("Termina controller para borrar roles con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para borrar permisos con error {} ",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	}
	
	@PatchMapping("/removeList")
	@ApiOperation(value = "Servicio para remover permisos a un rol, parametro { idRol y idPermissions }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> removePermissionsToRol(@RequestParam(required=true) Long idRol,
			@RequestParam(required=true) List<Long> idPermissions,HttpServletRequest request){
		String mensaje="Problems in PermisosController @PatchMapping";
		log.info("Controller para asignar permiso a Rol {} ,{}",idRol,idPermissions);
		Map<String, Object> formData = new HashMap<>();
		
		try
		{
			tokenService.regresaUsuario(serviceUser, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			idPermissions.stream().forEach((idPermission) ->{
				UserDTO usuarioToken =null;
				try
				{
					usuarioToken = tokenService.regresaUsuario(serviceUser, jwutil, request);
				} catch (Exception e) {
					log.error("Error: {}",e.getMessage());
				}
				formData.put("result", service.removePermissionToRol(idRol,idPermission,usuarioToken));
			});
			log.info("Termina controller para borrar roles con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.data(formData)
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para borrar permisos con error {} ",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	}
	
	
	@GetMapping("/getUnassignByRolAndScreen")
	@ApiOperation(value = "Servicio para obtener permisos no asignados por rol y pantalla", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getUnassing(@RequestParam Long idRol, @RequestParam Long idPantalla, @RequestHeader("Authorization")String authorization){
		Map<String, Object> formData = new HashMap<>();
		try {
			formData.put("Mensaje", "Obtencion de pantallas por rol");
			List<MenuDTO> menu = service.findUnassing(idRol,idPantalla);
			formData.put("menu", menu);
		} catch (Exception e) {
			log.info("ERROR EN MENU");
		}
		
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
				.mensaje(Mensajes.MSG_EXITO.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.data(formData)
				.build());
		
	}
	
	@GetMapping("/getAssignByRolAndScreen")
	@ApiOperation(value = "Servicio para obtener permisos asignados por rol y pantalla", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getAssing(@RequestParam Long idRol, @RequestParam Long idPantalla, @RequestHeader("Authorization")String authorization){
		Map<String, Object> formData = new HashMap<>();
		try {
			formData.put("Mensaje", "Obtencion de pantallas por rol");
			List<MenuDTO> menu = service.findAssing(idRol,idPantalla);
			formData.put("menu", menu);
		} catch (Exception e) {
			log.info("ERROR EN MENU");
		}
		
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
				.mensaje(Mensajes.MSG_EXITO.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.data(formData)
				.build());
		
	}
}
