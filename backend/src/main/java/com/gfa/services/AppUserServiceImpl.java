package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.*;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
    @Override
    public AppUser registerUser(RegisterRequestDTO request) {

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!request.getPassword().matches(passwordPattern)) {
            throw new IllegalArgumentException("Password must contain at least 8 characters, including at least 1 lower case, 1 upper case, 1 number, and 1 special character.");
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        //TODO:Wait for Matěj's spring security integration for password encoding.
        //Mocked encoding: For now just sets the password directly.
        newUser.setPassword(request.getPassword());

        String code = generateActivationCode();
        ActivationCode activationCode = new ActivationCode(code, newUser);

        AppUser savedUser = appUserRepository.save(newUser);
        activationCodeRepository.save(activationCode);

        activationCode.setAppUser(savedUser);

        // TODO: Integrate Daniel's email utility here to send activation code to the user.
        //Mock email sending
        System.out.println("Email would be sent here with activation code. For now, retrieve the activation code from the database for testing.");
        return savedUser;
    }

    @Override
    public void activateAccount(String code) {
        Optional<ActivationCode> activationCodeOpt = activationCodeRepository.findByActivationCodeContains(code);

        if (!activationCodeOpt.isPresent()) {
            throw new InvalidActivationCodeException("Invalid activation code.");
        }

        ActivationCode activationCode = activationCodeOpt.get();
        AppUser appUser = activationCode.getAppUser();

        if (appUser.isActive()) {
            throw new IllegalStateException("User account is already active.");
        }

        LocalDateTime activationCodeCreationTime = activationCode.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = activationCodeCreationTime.plusDays(1);

        if (now.isAfter(expirationTime)) {
            throw new IllegalStateException("Activation code has expired.");
        }

        appUser.setActive(true);
        appUserRepository.save(appUser);

        activationCodeRepository.delete(activationCode); // Do we want to delete the activation code after using it?
    }

    private String generateActivationCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 48;
        SecureRandom secureRandom = new SecureRandom();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();
    }
}
