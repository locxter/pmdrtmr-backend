package com.github.locxter.pmdrtmr.backend.lib;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// User repository class
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Method to find a user by it's username
    Optional<User> findByUsername(String username);

    // Method to check if a user with a given username exists
    Boolean existsByUsername(String username);
}
