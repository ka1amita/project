package com.gfa.services;

import com.gfa.models.ActivationCode;

public interface ActivationCodeService {

    ActivationCode saveActivationCode(ActivationCode activationCode);

    void deleteActivationCode(ActivationCode activationCode);
}
