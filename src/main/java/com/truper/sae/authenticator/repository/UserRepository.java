package com.truper.sae.authenticator.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.truper.sae.authenticator.entities.Usuario;
@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByuserName(String username);
}
	