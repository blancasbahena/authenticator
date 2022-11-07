package com.truper.saen.authenticator.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.truper.saen.authenticator.configuration.JWUtil;
import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.authenticator.service.ValidaTokenService;
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
@RequestMapping(value = "/usuarios" )
@Slf4j
public class UsuarioController { 
	private final  UserService service;
	private final JWUtil jwutil;
	private final ValidaTokenService tokenService;
	@GetMapping //(consumes =  MediaType.APPLICATION_JSON_UTF8_VALUE, produces  = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "Servicio que muestra informacion de un usuario, parametros {username o id}", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getUsuarios(@RequestParam(required=false) String userName,@RequestParam(required=false) Long id){
		log.info("Inicia controller para obtencion de usuarios {} , {} ,{}",userName!=null?userName:"-",id!=null?id:"-",Fechas.getHoraLogeo());
		String mensaje="Problems in UsuarioController @ GetMapping";
		try
		{
			Map<String, Object> formData = new HashMap<>();
			boolean exito=false;
			if(userName!=null  && !userName.isEmpty() && !userName.equals("")) {
				UserDTO userDTO= service.findByUserName(userName);
				if(userDTO!=null) {
					formData.put("usuario", userDTO);
					exito=true;
				}else {
					return ResponseEntity.ok(ResponseVO.builder()
							.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
							.mensaje(Mensajes.MSG_NODATA.getMensaje())
							.folio(ResponseVO.getFolioActual())
							.build());
				}
				
			}
			else if(id!=null) {
				UserDTO userDTO= service.findById(id);
				if(userDTO!=null) {
					formData.put("usuario", userDTO);
					exito=true;
				}else {
					return ResponseEntity.ok(ResponseVO.builder()
							.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
							.mensaje(Mensajes.MSG_NODATA.getMensaje())
							.folio(ResponseVO.getFolioActual())
							.build());
				}
			}
			else {
				log.info("Termina controller para obtencion de usuarios con error(1) {}",Fechas.getHoraLogeo());
				return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.folio(ResponseVO.getFolioActual())
					.mensaje(Mensajes.MSG_NODATA.getMensaje())
					.build());
			}
			if(exito) {
				log.info("Termina controller para obtencion de usuarios con exito {}",Fechas.getHoraLogeo());
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
		log.info("Termina controller para obtencion de usuarios con error(2) {} ",Fechas.getHoraLogeo() );
		return ResponseEntity.ok(ResponseVO.builder()
			.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
			.folio(ResponseVO.getFolioActual())
			.mensaje(mensaje)
			.build());
	}

	@PostMapping
	@ApiOperation(value = "Servicio para crear un usuario con o sin roles, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> saveUsuarios(@RequestBody UserDTO dto,HttpServletRequest request){
		log.info("Controller para guardar usuarios  {} {} ",dto.toString(),Fechas.getHoraLogeo());
		String mensaje="Problems in UsuarioController @ PostMapping";
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{
			usuarioToken = tokenService.regresaUsuario(service, jwutil, request);
		} catch (Exception e) {
			log.error("Error: {}",e.getMessage());
			mensaje =  e.getMessage();
			return ResponseEntity.ok(ResponseVO.builder()
					.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
					.mensaje(mensaje)
					.build());
		}
		try
		{
			dto.setUserCreated(usuarioToken);
			formData.put("result", service.save(dto));
			log.info("Termina controller para guardar usuarios termina con exito {}",Fechas.getHoraLogeo());
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
		log.info("Termina controller para guardar usuarios terminad con error {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.mensaje(mensaje)
				.folio(ResponseVO.getFolioActual())
				.build());
	}
	@PutMapping 
	@ApiOperation(value = "Servicio para modificar un usuario incluyendo roles, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> modifyUsuariosYroles(@RequestBody UserDTO dto,HttpServletRequest request){
		log.info("Controller para modificar usuarios {}  {}",dto.toString(),Fechas.getHoraLogeo());
		boolean modificarRoles=true;
		boolean soloPassword=false;
		return modifyUsuarios(dto,request,modificarRoles,soloPassword);
	} 
	@PutMapping("/detail") 
	@ApiOperation(value = "Servicio para modificar los detalles un usuario no incluye roles, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> modifyUsuariosOnlyDetail(@RequestBody UserDTO dto,HttpServletRequest request){
		log.info("Controller para modificar usuarios {}  {}",dto.toString(),Fechas.getHoraLogeo());
		boolean modificarRoles=false;
		boolean soloPassword=false;
		return modifyUsuarios(dto,request,modificarRoles,soloPassword);
	} 
	@PutMapping("/password") 
	@ApiOperation(value = "Servicio para modificar solo el password del usuario no incluye roles, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> modifyUsuarioPassword(@RequestBody UserDTO dto,HttpServletRequest request){
		boolean modificarRoles=false;
		boolean soloPassword=true;
		log.info("Controller para modificar usuarios {}  {}",dto.toString(),Fechas.getHoraLogeo());
		return modifyUsuarios(dto,request,modificarRoles,soloPassword);
		
	} 
	public ResponseEntity<ResponseVO> modifyUsuarios(@RequestBody UserDTO dto,HttpServletRequest request,boolean modificarRoles,boolean soloPassword) {
		String mensaje="Problems in UsuarioController @ PutMapping";
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{	
			usuarioToken = tokenService.regresaUsuario(service, jwutil, request);
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
			formData.put("result", service.update(dto,modificarRoles,soloPassword));
			log.info("Termina controller para modificar usuarios termina con exito {}",Fechas.getHoraLogeo());
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
		log.info("Termina controller para modificar usuarios termina con error {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	} 
	
	@DeleteMapping 
	@ApiOperation(value = "Servicio para eleminar logicamente un usuario, parametros { DTO }", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> deleteUsuarios(@RequestBody UserDTO dto,HttpServletRequest request){
		String mensaje="Problems in UsuarioController @ DeleteMapping";
		log.info("Controller para borrar usuarios {}  {}",dto.toString(),Fechas.getHoraLogeo());
		Map<String, Object> formData = new HashMap<>();
		UserDTO usuarioToken =null;
		try
		{
			usuarioToken = tokenService.regresaUsuario(service, jwutil, request);
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
			log.info("Termina controller para borrar usuarios termina con exito {}",Fechas.getHoraLogeo());
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
		log.info("Termina controller para borrar usuarios termina con error {}",Fechas.getHoraLogeo());
		return ResponseEntity.ok(ResponseVO.builder()
				.tipoMensaje(Mensajes.TIPO_ERROR.getMensaje())
				.folio(ResponseVO.getFolioActual())
				.mensaje(mensaje)
				.build());
	}
}
