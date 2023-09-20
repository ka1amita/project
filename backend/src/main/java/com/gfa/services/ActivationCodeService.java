package com.gfa.services;

import com.gfa.models.ActivationCode;

import java.util.Optional;

public interface ActivationCodeService {

    ActivationCode saveActivationCode(ActivationCode activationCode);

    void deleteActivationCode(ActivationCode activationCode);

    Optional<ActivationCode> findByActivationCodeContains(String activationCode);
}
