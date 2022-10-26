package com.truper.saen.configuration;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServices implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String usuario="example";
		String password="$2a$10$e6FO0wZ.ILZRfb.m0wXnc.T7nt0VSYneZrcknW5mjEly9x/THCToO";
		//request=LeerBase Datos(userName);
		return new User(usuario,password,new ArrayList<>());
	}


}
