package com.truper.saen.authenticator;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.truper.saen.authenticator.service.RolService;
import com.truper.saen.commons.dto.RoleDTO;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootTest
public class ValidaServiceRolesTest {
	@Autowired
	private RolService service;
	@BeforeEach
	public void setupUser(TestInfo info , TestReporter report) {
		log.info("Datos por metodo {} - {} ",info.getDisplayName(),info.getTestMethod());  
		AuthenticatorTest.revisaSystemPropertiesProfile();
	}
	@Test
	public void validaRepositoryTest() {  
		List<RoleDTO> roles=service.findAll();
		if(service!=null) {
			log.info("Roles tiene un total de {} roles ",roles.size());
		}else {
			log.error("PROBLEMAS  El servicio es completamente nulo");
		}
		assertNotNull(roles); 
	}
}
