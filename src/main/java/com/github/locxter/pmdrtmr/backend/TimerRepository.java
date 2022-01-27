// Setting the package
package com.github.locxter.pmdrtmr.backend;

// Including needed classes/interfaces
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TimerRepository class
@Repository
public interface TimerRepository extends JpaRepository<Timer, Long>
{
    // Function to find all timers of a user
    List<Timer> findByUserId(Long userId);
    
    // Function to find a timer of a user
    Optional<Timer> findByUserIdAndId(Long userId, Long id);
}
