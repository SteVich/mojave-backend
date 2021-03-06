package com.mojave.repository;

import com.mojave.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findUserByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
