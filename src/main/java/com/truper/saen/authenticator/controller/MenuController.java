package com.truper.saen.authenticator.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.truper.saen.authenticator.projection.PermisoProjection;
import com.truper.saen.authenticator.service.PermisosService;
import com.truper.saen.commons.dto.MenuDTO;
import com.truper.saen.commons.dto.PermisoDTO;
import com.truper.saen.commons.dto.ResponseVO;
import com.truper.saen.commons.enums.Mensajes;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/menu")
@Slf4j
@CrossOrigin(value = {"*"}, exposedHeaders = {"Content-Disposition"})
public class MenuController {
	
	private final PermisosService service;
	
	@GetMapping
	@ApiOperation(value = "Servicio para obtener el menu por usuario", authorizations = @Authorization(value = "Bearer"))
	public ResponseEntity<ResponseVO> getMenu(@RequestParam Long idUser, @RequestHeader("Authorization")String authorization){
		Map<String, Object> formData = new HashMap<>();
		try {
			formData.put("Mensaje", "Obtencion de menu por IdUser");
			List<MenuDTO> menu = service.findPermisosMenu(idUser);
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
