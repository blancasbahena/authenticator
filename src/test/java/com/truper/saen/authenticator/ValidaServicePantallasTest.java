package com.truper.saen.authenticator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import com.truper.saen.authenticator.service.PermisosService;
import com.truper.saen.authenticator.service.RolService;
import com.truper.saen.commons.dto.MenuDTO;
import com.truper.saen.commons.dto.RoleDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ValidaServicePantallasTest {

	@Autowired
	private PermisosService serviceP;

	@Test
	public void listarPantallas() {
		log.info("entra listarPantallas");
		List<MenuDTO> menu = serviceP.findPantallasMenu(new Long("32"));
		menu.stream().forEach((pantallas) -> {
			System.out.println(pantallas.getDescripcion());
			if (pantallas.getSubMenus() != null) {
				pantallas.getSubMenus().stream().forEach((subpantallas) -> {
					System.out.println("****" + subpantallas.getDescripcion());
				});
			}
		});
	}

	@Test
	public void listarNoAsignados() {
		log.info("entra listarNoAsignados");
		List<MenuDTO> menu = serviceP.findUnassing(new Long("36"), new Long("15"));
		menu.stream().forEach((pantallas) -> {
			System.out.println(pantallas.getDescripcion());
		});
	}

	@Test
	public void listarAsignados() {
		log.info("entra listarAsignados");
		List<MenuDTO> menu = serviceP.findAssing(new Long("36"), new Long("162"));
		menu.stream().forEach((pantallas) -> {
			System.out.println(pantallas.getDescripcion());
		});
	}
}
