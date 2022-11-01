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
public class TokenPermisoDTO implements Serializable {
 
	private static final long serialVersionUID = 4421355673508861745L;

	private Long id;
	
	private String descripcion;

	private String nombrePermiso;

	private Long parent;

	private String tipo;

	private String url;

	private String icon;

	private String identifierAccion;

	private String tooltip;

}
