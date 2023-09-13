package com.gfa.services;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.PasswordResetResponseDTO;
import com.gfa.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private final AppUserRepository appUserRepository;
    @Autowired
    private final ActivationCodeRepository activationCodeRepository;

    public AppUserServiceImpl(AppUserRepository appUserRepository, ActivationCodeRepository activationCodeRepository) {
        this.appUserRepository = appUserRepository;
        this.activationCodeRepository = activationCodeRepository;
    }

    @Override
    public ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO) {
        if (passwordResetRequestDTO == null) {
            throw new IllegalArgumentException("You have to provide either the username or the email field!");
        }
        passwordResetRequestDTO.setEmail(Objects.toString(passwordResetRequestDTO.getEmail(), ""));
        passwordResetRequestDTO.setUsername(Objects.toString(passwordResetRequestDTO.getUsername(), ""));
        if (passwordResetRequestDTO.getEmail().isEmpty() && passwordResetRequestDTO.getUsername().isEmpty()) {
            throw new IllegalArgumentException("You have to provide either the username or the email field!");
        }

        Optional<AppUser> appUser;
        if (!passwordResetRequestDTO.getEmail().isEmpty() && !passwordResetRequestDTO.getUsername().isEmpty()) {
            appUser = appUserRepository.findByEmailContainsAndUsernameContains(passwordResetRequestDTO.getEmail(), passwordResetRequestDTO.getUsername());
        } else if (!passwordResetRequestDTO.getEmail().isEmpty()) {
            appUser = appUserRepository.findByEmailContains(passwordResetRequestDTO.getEmail());
        } else {
            appUser = appUserRepository.findByUsernameContains(passwordResetRequestDTO.getEmail());
        }

        if (appUser.isPresent()) {
            ActivationCode activationCode = activationCodeRepository.save(new ActivationCode(generateResetCode(), appUser.get()));
            return ResponseEntity.ok(new PasswordResetResponseDTO(activationCode.getActivationCode()));
        } else {
            throw new IllegalArgumentException("User doesn't exist!");
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, String resetCode) {
        Optional<ActivationCode> activationCode = activationCodeRepository.findByActivationCodeContains(resetCode);
        // TODO: The 10 minute expire check shouldn't be here OR at least the 10 minutes shouldn't be here!
        if (activationCode.isPresent() && activationCode.get().getCreatedAt().plusMinutes(10).isAfter(LocalDateTime.now())) {
            if (passwordResetWithCodeRequestDTO.getPassword() != null && !passwordResetWithCodeRequestDTO.getPassword().isEmpty()) {
                // TODO: Some more advanced password validation (maybe in a Util class)...
                AppUser appUser = activationCode.get().getAppUser();
                appUser.setPassword(passwordResetWithCodeRequestDTO.getPassword());
                appUserRepository.save(appUser);
                activationCodeRepository.delete(activationCode.get());
            } else {
                throw new IllegalArgumentException("Password can't be empty!");
            }
        } else {
            throw new IllegalArgumentException("Reset code doesn't exist!");
        }
        return ResponseEntity.ok(new PasswordResetWithCodeResponseDTO("success"));
    }

    private String generateResetCode() {
        Random random = new Random();
        return random.ints('a', 'z' + 1)
                .limit(48)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
