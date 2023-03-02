package com.truper.saen.authenticator.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResetPasswordDTO implements Serializable{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8002453701138154036L;
	private String nombre;
	private String correo;
	private String copia;
	private String subject;
	private Integer numeroPlantilla;
	private String usuario; 
}