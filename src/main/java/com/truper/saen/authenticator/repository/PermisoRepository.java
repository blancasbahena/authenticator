package com.truper.saen.authenticator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.truper.saen.commons.entities.Permiso;
@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {

	Optional<Permiso> findByNombrePermiso(String string);

	List<Permiso> findByIdIn(List<Long> ids);

 

}
