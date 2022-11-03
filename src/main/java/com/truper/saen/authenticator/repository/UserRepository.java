package com.truper.saen.authenticator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.truper.saen.authenticator.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 

	Optional<User> findByUserNameAndActive(String userName, boolean b);

	Optional<User> findByEmailAndActive(String email, boolean b);

}
