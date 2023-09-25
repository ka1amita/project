package com.gfa.repositories;

import com.gfa.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByEmailAndUsername(String email, String username);
    Optional<AppUser> findByUsernameOrEmail(String username, String email);
    @Query(value = "SELECT * FROM app_users WHERE deleted = true", nativeQuery = true)
    List<AppUser> findAllDeletedAppUsers();
}

