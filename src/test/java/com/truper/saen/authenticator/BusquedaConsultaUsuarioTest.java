package com.truper.saen.authenticator;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.truper.saen.authenticator.service.UserService;
import com.truper.saen.commons.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
class BusquedaConsultaUsuarioTest { 
	@Autowired
	private UserService userService; 
	private Boolean _true  = true; 
	boolean modificarRoles=false;
	boolean soloPassword=false;
	
	@BeforeEach
	public void setupUser(TestInfo info , TestReporter report) {
		log.info("Datos por metodo {} - {} ",info.getDisplayName(),info.getTestMethod());  
		AuthenticatorTest.revisaSystemPropertiesProfile(); 
	}
	@Test
    @Order(1)
	@DisplayName("Valida modicar usuario numero 1 y lo regresa a su valor correcto")
	void modificaYregresa() {
		try { 
			List<UserDTO> usuarios=userService.findall();
			if(usuarios!=null && !usuarios.isEmpty()) {
				UserDTO manager = usuarios.stream().findFirst().get();
				if(manager!=null) {
					boolean active  =manager.getActive();
					String password  = manager.getPassword();
					UserDTO dtoUserNuevo = userService.findByUserName(manager.getUserName());
					if(dtoUserNuevo==null) {
						modificarRoles=false;
						soloPassword=false;
						dtoUserNuevo.setActive(!active);
						Boolean result =userService.save(dtoUserNuevo);
						assertThat(result.booleanValue()).isEqualTo(_true);
						 
						log.info("Devolver++++++++++++++++++++++++++++++++ {} ", manager.getUserName());
						modificarRoles=false;
						soloPassword=false;
						dtoUserNuevo.setActive(active);
						Boolean result3 =userService.save(dtoUserNuevo);
						assertThat(result3.booleanValue()).isEqualTo(_true); 
						
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}