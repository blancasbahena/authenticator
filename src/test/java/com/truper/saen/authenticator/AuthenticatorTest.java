package com.truper.saen.authenticator;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;
import java.util.Properties;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.springframework.boot.test.context.SpringBootTest;

import com.truper.saen.commons.dto.AuthenticationRequest;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@SpringBootTest
public class AuthenticatorTest {
	private static AuthenticationRequest request=null;
	private static String propertyProfile=null;
	@BeforeAll
	public static void imprimirInicio() {
		log.info("Inicio de Test");
		request = new AuthenticationRequest("usuario","password");
		revisaSystemPropertiesProfile();
	}
	@BeforeEach
	public void setupUser(TestInfo info , TestReporter report) {
		log.info("Datos por metodo {} - {} ",info.getDisplayName(),info.getTestMethod()); 
		
	}
	@Test 
	@DisplayName("Probando Decodificacion Base 64 para authenticator")
	void validaParametroEntrada() {
		log.info("Valida parametro entrada");
		request = new AuthenticationRequest("QXV0aDNudDFjNHQzUzQzTjRjMTBuNGwzNQ==","MzIxMzIx");
		String usuario = new String(Base64.getDecoder().decode(request.getUsername()));
		String password = new String(Base64.getDecoder().decode(request.getPassword()));
		assertEquals("Auth3nt1c4t3S43N4c10n4l35",usuario,"No decodifico Base64 con exito");
		assertAll(
				() -> assertEquals("321321",password,"No decodifico Base64 con exito"),
				() -> assertTrue("321321".equals(password),"No decodifico Base64 con exito")
				); 
	}
	@AfterEach
	public void liberaGarbage() { 
		request =null;
	}
	
	@AfterAll
	public static void imprimirfinal() {
		log.info("final de Test");
	}
	@Test
	@EnabledOnOs(OS.WINDOWS)
	void seEjecutaEnsoloWinwods(){
		log.info("Se ejecuta solo en ambiente Windows");
	}
	@Test
	@EnabledOnOs({OS.MAC,OS.LINUX})
	void seEjecutaEnMacOLinux(){
		log.info("Se ejecuta en ambientes Linux o Mac");
	}
	@Test
	@DisabledOnOs(OS.WINDOWS)
	void noSeEjecutaEnWindows(){
		log.info("No se ejecuta en ambientes windows");
	}
	@Test
	@EnabledOnJre(JRE.JAVA_8)
	public void soloEnJava8() {
		log.info("Solo se ejecuta en Java 8");
	}
	@Test
	@EnabledOnJre(JRE.JAVA_15)
	public void soloEnJava15() {
		log.info("Solo se ejecuta en Java 15");
	}
	@Test
	@DisplayName("😱 Probando que haya spring.profiles.active")
	//@EnabledIfSystemProperty(named="spring.profiles.active",matches=PROFILE)
	public void imprimeSystemProperties() {
		log.info("Se imprime system properties");
		
		Properties properties= System.getProperties();
		properties
		.forEach((key,value)->{ 
			if(key.equals("spring.profiles.active")) {
				propertyProfile= value.toString();
			}
		});
		assertNotNull(propertyProfile," No se encontro el valor de : spring.profiles.active"); 
	}
	@RepeatedTest(value=3 , 
			name="{displayName} Revisando que haya spring.profiles.active {currentRepetition} de {totalRepetitions}")
 
	public static void revisaSystemPropertiesProfile() {
		log.info(" Se valida que haya system propery spring.profiles.active");
		
		Properties properties= System.getProperties();
		properties
		.forEach((key,value)->{ 
			if(key.equals("spring.profiles.active")) {
				propertyProfile= value.toString();
			}
		});
		assertNotNull(propertyProfile," No se encontro el valor de : spring.profiles.active"); 
	}
}
