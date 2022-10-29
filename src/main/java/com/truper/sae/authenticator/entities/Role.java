package com.truper.sae.authenticator.entities;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "Roles")
public class Role implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String descripcion;

	private Boolean active;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usermodified", referencedColumnName = "id")
    private User usermodified;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usercreated", referencedColumnName = "id")
    private User usercreated;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "roles")
	@JsonIgnore
	private List<User> usuarios;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "roles_permisos", 
		joinColumns = { @JoinColumn(name = "id_rol") }, 
		inverseJoinColumns = {@JoinColumn(name = "id_permisos") })
	private List<Permiso> permisos;

	
	/**
	 * @param id
	 */
	public Role(long id) {
		super();
		this.id = id;
	}
	
}