package com.gfa.repositories;

import com.gfa.models.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivationCodeRepo extends JpaRepository<ActivationCode, Long> {
}
