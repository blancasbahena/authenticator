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
import com.truper.saen.authenticator.service.RolService;
import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.authenticator.service.ValidaTokenService;
import com.truper.saen.commons.dto.ResponseVO;
import com.truper.saen.commons.dto.RoleDTO;
import com.truper.saen.commons.dto.UserDTO;
import com.truper.saen.commons.enums.Mensajes;
import com.truper.saen.commons.utils.Fechas;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/roles")
@Slf4j
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class RolesController { 
	private final RolService service;
	private final UserService serviceUser;
	private final JWUtil jwutil;
	private final ValidaTokenService tokenService;
	@GetMapping 
	@ApiOperation(value = "Servicio que regresa todos o un role, parametros { vacios, idUser o idRol } ", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> get(@RequestParam(required=false) Long idUser,@RequestParam(required=false) Long idRol
			,@RequestHeader("Authorization") String authorization){
		String mensaje="Problems in RolesController @ GetMapping";
		log.info("Inicia controller para obtencion de roles {} , {} , {} ",idRol!=null?idRol:"-",idUser!=null?idUser:"-",Fechas.getHoraLogeo());
		try
		{
			Map<String, Object> formData = new HashMap<>();
			boolean exito=false;
			if(idUser!=null) {
				List<RoleDTO> roles= service.findByUser(idUser);
				if(roles!=null && !roles.isEmpty()) {
					formData.put("roles", roles);
					exito=true;
				}
			}
			else if(idRol!=null) {
				RoleDTO rol= service.findById(idRol);
				if(rol!=null) {
					formData.put("rol", rol);
					exito=true;
				}
			}
			else if(idUser==null && idRol==null) {
				List<RoleDTO> roles=service.findAll();
				if(roles!=null && !roles.isEmpty()) {
					formData.put("roles", roles);
					exito=true;
				}
			}
			else {
				log.info("Termina controller para obtencion de roles con error {}",Fechas.getHoraLogeo());
				return ResponseEntity.ok(ResponseVO.builder()
						.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
						.mensaje(Mensajes.MSG_NODATA.getMensaje())
						.folio(ResponseVO.getFolioActual())
						.build());
			}
			if(exito) {
				log.info("Termina controller para obtencion de roles con exito {}",Fechas.getHoraLogeo());
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
		log.info("Termina controller para obtencion de roles con error(2) {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
			.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
			.folio(ResponseVO.getFolioActual())
			.mensaje(mensaje)
			.build());
	}

	@PostMapping
	@ApiOperation(value = "Servicio para crear un rol con o sin permisos, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> save(@RequestBody RoleDTO dto,HttpServletRequest request){
		log.info("Controller para guardar roles  {} , {} ",dto.toString(),Fechas.getHoraLogeo());
		String mensaje="Problems in RolesController @ PostMapping";
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
					.mensaje(mensaje)
					.folio(ResponseVO.getFolioActual())
					.build());
		}
		try
		{
			dto.setUserCreated(usuarioToken);
			formData.put("result", service.save(dto));
			log.info("Termina controller para guardar roles con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.folio(ResponseVO.getFolioActual())
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para guardar roles con error {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.folio(ResponseVO.getFolioActual())
				.build());
	}
	@PutMapping 
	@ApiOperation(value = "Servicio para modificar rol y permisos, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> modifyallDTO(@RequestBody RoleDTO dto,HttpServletRequest request){
		log.info("Controller para modificar roles {}  , {}",dto.toString(),Fechas.getHoraLogeo());
		boolean modifyPermission=true;
		return modify(dto,request,modifyPermission);
	}
	@PutMapping("/detail")
	@ApiOperation(value = "Servicio para modificar solo detalle de role, parametros { DTO } ", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> modifyDetail(@RequestBody RoleDTO dto,HttpServletRequest request){
		log.info("Controller para modificar solo el detalle del rol {}  , {}",dto.toString(),Fechas.getHoraLogeo());
		boolean modifyPermission=false;
		return modify(dto,request,modifyPermission);
	}
	public ResponseEntity<ResponseVO> modify(@RequestBody RoleDTO dto,HttpServletRequest request,boolean modifyPermission){
		String mensaje="Problems in RolesController @ PutMapping";
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
					.mensaje(mensaje)
					.folio(ResponseVO.getFolioActual())
					.build());
		}
		try
		{
			dto.setUserModified(usuarioToken);
			formData.put("result", service.update(dto,modifyPermission));
			log.info("Termina controller para modificar roles con exito {}",Fechas.getHoraLogeo()) ;
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.folio(ResponseVO.getFolioActual())
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para modificar roles con erorr {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.folio(ResponseVO.getFolioActual())
				.build());
	} 
	
	@DeleteMapping 
	@ApiOperation(value = "Servicio para borrar roles, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> delete(@RequestBody RoleDTO dto,HttpServletRequest request){
		String mensaje="Problems in RolesController @ DeleteMapping";
		log.info("Servicio para borrar roles {} ",dto.toString());
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
					.mensaje(mensaje)
					.folio(ResponseVO.getFolioActual())
					.build());
		}
		try
		{
			dto.setUserModified(usuarioToken);
			formData.put("result", service.delete(dto));
			log.info("Termina controller para borrar roles  con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.folio(ResponseVO.getFolioActual())
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para borrar roles  con error {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.folio(ResponseVO.getFolioActual())
				.build());
	}
	
	@PatchMapping("/append")
	@ApiOperation(value = "Servicio para agregar un rol a un usuario, parametros { idUser y idRol }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> appendRoleToUser(@RequestParam(required=true) Long idUser,@RequestParam(required=true) Long idRol
			,HttpServletRequest request){
		String mensaje="Problems in PermisosController @PatchMapping";
		log.info("Controller para asignar permiso a Rol {} ,{}",idUser,idRol);
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
					.mensaje(mensaje)
					.folio(ResponseVO.getFolioActual())
					.build());
		}
		try
		{
			
			formData.put("result", service.appendRoleToUser(idUser,idRol,usuarioToken));
			log.info("Termina controller para borrar roles con exito {}",Fechas.getHoraLogeo());
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
					.mensaje(Mensajes.MSG_EXITO.getMensaje())
					.data(formData)
					.folio(ResponseVO.getFolioActual())
					.build());
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		log.info("Termina controller para borrar permisos con error {} ",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.folio(ResponseVO.getFolioActual())
				.build());
	}
	@PatchMapping("/remove")
	@ApiOperation(value = "Servicio para remover un rol a un usuario,parametros { idUser y idRol }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> RoleToUser(@RequestParam(required=true) Long idUser,@RequestParam(required=true) Long idRol
			,HttpServletRequest request){
		String mensaje="Problems in PermisosController @PatchMapping";
		log.info("Controller para asignar permiso a Rol {} ,{}",idUser,idRol);
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
			
			formData.put("result", service.removeRoleToUser(idUser,idRol,usuarioToken));
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
	@GetMapping("/assigned")
	@ApiOperation(value = "Servicio que regresa todos roles asignados a user{ idUser } ", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getuAssigned (@RequestParam(required=false) Long idUser,@RequestHeader("Authorization") String authorization){
		String mensaje=Mensajes.MSG_NODATA.getMensaje();
		log.info("Inicia controller para obtencion de roles {} , {} , {} ", idUser!=null?idUser:"-",Fechas.getHoraLogeo());
		try
		{
			Map<String, Object> formData = new HashMap<>();
			if(idUser!=null) {
				List<RoleDTO> roles= service.findByUser(idUser);
				if(roles!=null && !roles.isEmpty()) {
					formData.put("roles", roles);
					return ResponseEntity.ok(ResponseVO.builder()
							.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
							.mensaje(Mensajes.MSG_EXITO.getMensaje())
							.folio(ResponseVO.getFolioActual())
							.data(formData)
							.build());
				}
			}
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		return ResponseEntity.ok(ResponseVO.builder()
			.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
			.folio(ResponseVO.getFolioActual())
			.mensaje(mensaje)
			.build());
	}
	@GetMapping("/unassigned")
	@ApiOperation(value = "Servicio que regresa todos roles no asignados a user{ idUser } ", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getUnassigned (@RequestParam(required=false) Long idUser,@RequestHeader("Authorization") String authorization){
		String mensaje=Mensajes.MSG_NODATA.getMensaje();
		log.info("Inicia controller para obtencion de roles {} , {} , {} ", idUser!=null?idUser:"-",Fechas.getHoraLogeo());
		try
		{
			Map<String, Object> formData = new HashMap<>();
			if(idUser!=null) {
				List<RoleDTO> roles= service.findByUserUnassigned(idUser);
				if(roles!=null && !roles.isEmpty()) {
					formData.put("roles", roles);
					return ResponseEntity.ok(ResponseVO.builder()
							.tipoMensaje(Mensajes.TIPO_EXITO.getMensaje())
							.mensaje(Mensajes.MSG_EXITO.getMensaje())
							.folio(ResponseVO.getFolioActual())
							.data(formData)
							.build());
				}
			}
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
		}
		return ResponseEntity.ok(ResponseVO.builder()
			.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
			.folio(ResponseVO.getFolioActual())
			.mensaje(mensaje)
			.build());
	}
}
