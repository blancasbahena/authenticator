package com.truper.saen.commons.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class UserDTO implements Serializable {
	private static final long serialVersionUID = 3398541430416849601L;

	private Long id;

	private Boolean active;

	private String userName;

	private String name;

	private String password;

	private String email;

	private Boolean userAD;

	private Boolean pwdReset;

	private Date created;

	private UserDTO userModified;

	private Date modified;

	private UserDTO userCreated;

	private List<RoleDTO> roles;

}