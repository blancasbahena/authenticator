package com.truper.sae.authenticator.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;


	@Column(name = "username")
	private String userName;


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @param id
	 * @param userName
	 */
	public Usuario(long id, String userName) {
		super();
		this.id = id;
		this.userName = userName;
	}
 
	/** 
	 */
	public Usuario() {
		super();
	}
 
	
}
