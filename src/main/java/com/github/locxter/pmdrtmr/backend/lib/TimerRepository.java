package com.github.locxter.pmdrtmr.backend.lib;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Timer repository class
@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
    // Method to find all timers of a user
    List<Timer> findByUserId(Long userId);

    // Method to find a timer of a user
    Optional<Timer> findByUserIdAndId(Long userId, Long id);
}
