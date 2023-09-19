package com.gfa.services;

import com.gfa.models.ActivationCode;
import com.gfa.repositories.ActivationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
