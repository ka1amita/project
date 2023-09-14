package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
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
        Optional<AppUser> appUser = Optional.empty();
        if (passwordResetRequestDTO != null) {
            appUser = appUserRepository.findByEmailContainsAndUsernameContains(passwordResetRequestDTO.getEmail(), passwordResetRequestDTO.getUsername());
            if (!appUser.isPresent()) {
                appUser = appUserRepository.findByEmailContains(passwordResetRequestDTO.getEmail());
            }
            if (!appUser.isPresent()) {
                appUser = appUserRepository.findByUsernameContains(passwordResetRequestDTO.getUsername());
            }
        }

        if (appUser.isPresent()) {
            ActivationCode activationCode = activationCodeRepository.save(new ActivationCode(generateResetCode(), appUser.get()));
            return ResponseEntity.ok(new PasswordResetResponseDTO(activationCode.getActivationCode()));
        } else {
            throw new IllegalArgumentException("User not found!");
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

    @Override
    public LoginResponseDTO userLogin(Optional<LoginRequestDTO> loginRequestDto) {
        LoginRequestDTO payload = loginRequestDto.orElseThrow(() -> new NullPointerException("Input body was not received."));
        if (payload.getLoginInput() == null || payload.getLoginInput().isEmpty()) {
            throw new IllegalArgumentException("Please provide a name or an email address.");
        }
        if (payload.getPassword() == null || payload.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Please provide a password.");
        }
        AppUser appUser = appUserRepository.findByEmailContainsAndUsernameContains(payload.getLoginInput(), payload.getLoginInput())
                .orElseThrow(() -> new NullPointerException("The user can not be found in the database."));
        if (!appUser.getPassword().equals(payload.getPassword()))
            throw new IllegalArgumentException("The password is incorrect.");
        return new LoginResponseDTO("Demo token");
    }
}
