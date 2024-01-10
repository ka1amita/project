package com.matejkala.repositories;

import com.matejkala.models.ActivationCode;
import com.matejkala.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {
    Optional<ActivationCode> findByActivationCode(String activationCode);

    Optional<ActivationCode> findByAppUser(AppUser user);
}
