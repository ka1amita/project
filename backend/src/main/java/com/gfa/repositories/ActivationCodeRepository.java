package com.gfa.repositories;

import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {
    Optional<ActivationCode> findByActivationCode(String activationCode);

    Optional<ActivationCode> findByAppUser(AppUser user);
}
