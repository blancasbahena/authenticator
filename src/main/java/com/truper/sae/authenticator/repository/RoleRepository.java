package com.truper.sae.authenticator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.truper.sae.authenticator.entities.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByDescripcion(String descripcion);
 
 

}
