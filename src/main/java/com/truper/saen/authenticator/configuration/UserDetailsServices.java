package com.truper.saen.authenticator.configuration;


import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.truper.sae.commons.dto.UserDTO;
import com.truper.saen.authenticator.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailsServices implements UserDetailsService{
	private final  UserService service;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername >> {}",username);
		UserDTO usuario = service.findByUserName(username);
		if(usuario!=null) {
			if (username.equals(usuario.getUserName())) {
				if(usuario.getRoles()!=null) {
					if(!usuario.getRoles().isEmpty()) {
						return new User(username, usuario.getPassword(),usuario.getRoles().stream()
								.map(m->new SimpleGrantedAuthority((String)(m.getDescripcion()!=null?m.getDescripcion():""))).collect(Collectors.toSet()));
					}else {
						return new User(username, usuario.getPassword(),new ArrayList<>());
					}
				}
				return new User(username, usuario.getPassword(),new ArrayList<>());
			} else {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}		
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}