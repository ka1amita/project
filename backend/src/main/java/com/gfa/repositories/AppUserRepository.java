package com.gfa.repositories;

import com.gfa.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByEmailAndUsername(String email, String username);
    Optional<AppUser> findByUsernameOrEmail(String username, String email);

    Optional<AppUser> findAppUserByActivationCodesContaining(String activationCode);
    @Query("SELECT u FROM AppUser u JOIN u.activationCodes ac WHERE ac.activationCode = :activationCode")
    Optional<AppUser> findAppUserByActivationCode(@Param("activationCode") String activationCode);
}

