package com.truper.sae.commons.dto;

import java.io.Serializable;
import java.util.Date;
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
public class RoleDTO implements Serializable {

	private static final long serialVersionUID = -7380089516858626025L;

	private Long id;

	private String descripcion;

	private Boolean active;

	private Date created;

	private UserDTO userModified;

	private Date modified;

	private UserDTO userCreated;
	
	private List<PermisoDTO> permisos;

}