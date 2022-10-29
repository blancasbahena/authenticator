package com.truper.sae.commons.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor 
@Builder
public class AuthenticationResponse  implements Serializable{
	private final String jwt;
}
