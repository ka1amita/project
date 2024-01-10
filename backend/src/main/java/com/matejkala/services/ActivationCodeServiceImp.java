package com.matejkala.services;

import com.matejkala.models.ActivationCode;
import com.matejkala.repositories.ActivationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivationCodeServiceImp implements ActivationCodeService {

    private final ActivationCodeRepository activationCodeRepository;

    @Autowired
    public ActivationCodeServiceImp(ActivationCodeRepository activationCodeRepository) {
        this.activationCodeRepository = activationCodeRepository;
    }

    @Override
    public ActivationCode saveActivationCode(ActivationCode activationCode) {
        return activationCodeRepository.save(activationCode);
    }

    @Override
    public void deleteActivationCode(ActivationCode activationCode) {
        activationCodeRepository.delete(activationCode);
    }

    @Override
    public Optional<ActivationCode> findByActivationCodeContains(String activationCode) {
        return activationCodeRepository.findByActivationCode(activationCode);
    }
}
