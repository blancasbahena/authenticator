package com.truper.sae.authenticator.configuration;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.truper.sae.authenticator.entities.Usuario;
import com.truper.sae.authenticator.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailsServices implements UserDetailsService{
	private final UserRepository repositoryUser;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repositoryUser.findByuserName(username);
		if(usuario.isPresent()) {
			if (username.equals(usuario.get().getUserName())) {
				return new User(username, usuario.get().getPassword(),usuario.get().getRoles().stream()
						.map(m->new SimpleGrantedAuthority((String)m.getDescripcion())).collect(Collectors.toSet()));
			} else {
				throw new UsernameNotFoundException("User not found with username: " + username);
			}		
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}


}
