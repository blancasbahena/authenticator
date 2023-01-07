package com.truper.saen.authenticator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.truper.saen.authenticator.service.PermisosService;
import com.truper.saen.commons.dto.PermisoDTO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootTest
public class ValidaServicePermisosTest {
	@Autowired
	private PermisosService permisosService;
	@SuppressWarnings("deprecation")
	@BeforeEach
	public void setupUser(TestInfo info , TestReporter report) {
		log.info("Datos por metodo {} - {} ",info.getDisplayName(),info.getTestMethod()); 
		AuthenticatorTest.revisaSystemPropertiesProfile();
	}
	@Test
	public void validaRepositoryTest() {  
		List<PermisoDTO> permisos=permisosService.findAll(); 
		if(permisosService!=null) { 
			log.info("Permisos tiene un total de {} permisos ",permisos.size());
		}else {
			log.error("PROBLEMAS  El servicio es completamente nulo");
		}
		assertNotNull(permisos); 
	}
}
