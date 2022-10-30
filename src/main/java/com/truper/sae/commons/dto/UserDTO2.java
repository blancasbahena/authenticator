package com.truper.sae.commons.dto;

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
public class UserDTO2 implements Serializable {
	private static final long serialVersionUID = 3398541430416849601L;

	private Long id;

	private Boolean active;

	private String userName;

	private String name;

	private String password;

	private String email;

	private Boolean userAD;

}