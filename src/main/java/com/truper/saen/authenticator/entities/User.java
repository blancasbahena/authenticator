package com.truper.saen.authenticator.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Entity
@Table(name = "usuarios")
public class User implements Serializable{ 
 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Boolean active;

	@Column(name = "username")
	private String userName;
	
	@Column(name = "name")
	private String name;

	private String password;

	private String email;

	private Boolean userAD;

	@Column(name = "pwdreset")
	private Boolean pwdReset;

	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usermodified", referencedColumnName = "id")
    private User userModified;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usercreated", referencedColumnName = "id")
    private User userCreated;
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", 
		joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "id"))
	private List<Role> roles;


}