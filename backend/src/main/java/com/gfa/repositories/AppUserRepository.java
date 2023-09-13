package com.gfa.repositories;

import com.gfa.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsernameContains(String username);
    Optional<AppUser> findByEmailContains(String email);
    Optional<AppUser> findByEmailContainsAndUsernameContains(String email, String username);
}
