package com.truper.sae_nacionales.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.truper.ActiveDirectoryConnector;
import com.truper.sae_nacionales.dto.AuthenticationRequest;
import com.truper.sae_nacionales.dto.AuthenticationResponse;
import com.truper.sae_nacionales.security.JWUtil;
import com.truper.sae_nacionales.security.UserDetailsServices;

import lombok.RequiredArgsConstructor; 
@RequiredArgsConstructor
@RestController
@RequestMapping 
public class AuthenticateController { 
	private final AuthenticationManager authenticationManager;
	private final ActiveDirectoryConnector AD_CON = ActiveDirectoryConnector.getInstance();
	private final UserDetailsServices myUserDatails;  
	@GetMapping("/test")
    public String[] getLista() {
		String[] lista= {"A", "B", "C"};
		return lista;
    } 
	@RequestMapping(value ="/authenticate" ,  method = RequestMethod.POST)
	public ResponseEntity<?> creareAutentication(@RequestBody AuthenticationRequest request) throws Exception{
		String jwt ="";
		boolean existeBD=true;
		try
		{
			//busco primero el username en Base de datos
			//es de AD
			//Valido el AD
			AD_CON.validaLogin(request.getUsername(),request.getPassword());
			
			//Guardar (request);
			
		}catch(BadCredentialsException  ex) {
			throw new Exception("Equivocacion de usuario / contraseña");
			//Validarlo en Base de datos.
		}
		if(existeBD) {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			final UserDetails userDetails = myUserDatails.loadUserByUsername(request.getUsername());
			jwt = JWUtil.generaToken(userDetails);
		}
		return  ResponseEntity.ok(new AuthenticationResponse(jwt));
				
	}
	@RequestMapping(value ="/creaTokenAPP" ,  method = RequestMethod.POST)
	public ResponseEntity<?> creareAutenticationTAPP(@RequestBody AuthenticationRequest request) throws Exception{
		try
		{
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		}catch(BadCredentialsException  ex) {
			throw new Exception("Equivocacion de usuario / contraseña");
		}
		final UserDetails userDetails = myUserDatails.loadUserByUsername(request.getUsername());
		final String jwt = JWUtil.generaToken(userDetails);
		return  ResponseEntity.ok(new AuthenticationResponse(jwt));				
	}
}