package com.truper.saen.commons.dto;

import java.util.List;

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
public class TokenDTO {

	private List<TokenRoleDTO> roles;
	
	private List<TokenPermisoDTO> permisos;
}
