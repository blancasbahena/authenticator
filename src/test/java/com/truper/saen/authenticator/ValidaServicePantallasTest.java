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
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

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
	//@BeforeEach
	public void setupUser(TestInfo info , TestReporter report) {
		log.info("Datos por metodo {} - {} ",info.getDisplayName(),info.getTestMethod());  
		AuthenticatorTest.revisaSystemPropertiesProfile();
	}
	
	@Test
	public void listarTest() {
		log.info("entra");
		List<MenuDTO> menu = serviceP.findPantallasMenu(new Long("32"));
		menu.stream().forEach((pantallas) ->{
			System.out.println(pantallas.getDescripcion());
			if(pantallas.getSubMenus() != null) {
				pantallas.getSubMenus().stream().forEach((subpantallas) ->{
					System.out.println("****"+subpantallas.getDescripcion());
				});
			}
		});
	}
}
