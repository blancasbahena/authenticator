package com.truper.saen.commons.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PermisoDTO implements Serializable {
 
	private static final long serialVersionUID = 4421355673508861745L;

	private Long id;
	
	private Boolean active;

	private String descripcion;

	private String nombrePermiso;

	private PermisoDTO parent;

	private String tipo;

	private String url;

	private String icon;
	
	private String identifierAccion;
	
	private String tooltip;
	
	private Date created;
	
	private UserDTO userModified;
	
    private Date modified;
    
	private UserDTO userCreated;	

}
