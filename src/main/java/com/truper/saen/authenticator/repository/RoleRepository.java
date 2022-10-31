package com.truper.saen.authenticator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.truper.saen.authenticator.entities.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByDescripcion(String descripcion);

	List<Role> findByIdIn(List<Long> collect);
 
 

}
