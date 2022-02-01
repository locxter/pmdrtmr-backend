package com.github.locxter.pmdrtmr.backend;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// UserRepository class
@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    // Function to find a user by it's username
    Optional<User> findByUsername(String username);

    // Function to check if a user with a given username exists
    Boolean existsByUsername(String username);
}
