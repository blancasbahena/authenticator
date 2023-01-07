package com.truper.saen.authenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class UserControllerTest {   
	@Autowired
    private MockMvc mockMvc;
 
	@BeforeEach
	public void setupUser(TestInfo info , TestReporter report) {
		log.info("Datos por metodo {} - {} ",info.getDisplayName(),info.getTestMethod());  
		AuthenticatorTest.revisaSystemPropertiesProfile(); 
	} 
	
}