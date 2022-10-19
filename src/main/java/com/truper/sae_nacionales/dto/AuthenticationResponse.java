package com.truper.sae_nacionales.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor 
public class AuthenticationResponse  implements Serializable{
	private final String jwt;
}
