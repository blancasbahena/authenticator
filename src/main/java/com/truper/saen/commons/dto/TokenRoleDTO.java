package com.truper.saen.commons.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TokenRoleDTO implements Serializable{ 

	private static final long serialVersionUID = -7380089516858626025L;

	private Long id;
	
	private String descripcion;

	
	
}