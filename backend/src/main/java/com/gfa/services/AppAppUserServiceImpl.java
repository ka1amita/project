package com.gfa.services;

import com.gfa.dtos.responsedtos.RegisterRequestDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppAppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final ActivationCodeRepository activationCodeRepository;
    //private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppAppUserServiceImpl(AppUserRepository appUserRepository, ActivationCodeRepository activationCodeRepository) {
        this.appUserRepository = appUserRepository;
        this.activationCodeRepository = activationCodeRepository;
        // this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser registerUser(RegisterRequestDTO request) {

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
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
        newUser.setActive(false);

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
        Optional<ActivationCode> activationCodeOpt = activationCodeRepository.findByActivationCode(code);

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

    @Override
    public boolean isUsernameUnique(String username) {
        return !appUserRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailUnique(String email) {
        return !appUserRepository.existsByEmail(email);
    }
}
