package com.gfa.services;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.*;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import com.gfa.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              ActivationCodeRepository activationCodeRepository,
                              RoleRepository roleRepository,
                              @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                              AuthenticationManager authenticationManager,
                              EmailService emailService) {

        this.appUserRepository = appUserRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @Override
    public ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO) throws MessagingException {
        Optional<AppUser> appUser = Optional.empty();
        if (passwordResetRequestDTO != null) {
            appUser = appUserRepository.findByEmailAndUsername(passwordResetRequestDTO.getEmail(), passwordResetRequestDTO.getUsername());
            if (!appUser.isPresent()) {
                appUser = appUserRepository.findByEmail(passwordResetRequestDTO.getEmail());
            }
            if (!appUser.isPresent()) {
                appUser = appUserRepository.findByUsername(passwordResetRequestDTO.getUsername());
            }
        }

        if (appUser.isPresent()) {
            ActivationCode activationCode = activationCodeRepository.save(new ActivationCode(generateResetCode(), appUser.get()));
            emailService.resetPasswordEmail(appUser.get().getEmail(), appUser.get().getUsername(), activationCode.getActivationCode());
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
                saveUser(appUser);
                activationCodeRepository.delete(activationCode.get());
            } else {
                throw new IllegalArgumentException("Password can't be empty!");
            }
        } else {
            throw new IllegalArgumentException("Reset code doesn't exist!");
        }
        return ResponseEntity.ok(new PasswordResetWithCodeResponseDTO("Password has been successfully changed."));
    }

    private String generateResetCode() {
        Random random = new Random();
        return random.ints('a', 'z' + 1)
                .limit(48)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public void addRoleToAppUser(String username, String roleName) {
        AppUser appUser =
                appUserRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Username not found in the DB"));
        Role role =
                roleRepository.findByName(roleName)
                        .orElseThrow(() -> new NoSuchElementException("Role" +
                                " name not found in the " +
                                "DB"));
        appUser.getRoles()
                .add(role);
    }

    @Override
    public void addRoleToAppUser(AppUser appUser, String roleName) {
        Role role =
                roleRepository.findByName(roleName)
                        .orElseThrow(() -> new NoSuchElementException("Role" +
                                " name not found in the " +
                                "DB"));
        appUser.getRoles()
                .add(role);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public ActivationCode saveActivationCode(ActivationCode activationCode) {
        return activationCodeRepository.save(activationCode);
    }

    @Override
    public AppUser getAppUser(String username) {
        Optional<AppUser> optAppUser = appUserRepository.findByUsername(username);
        AppUser appUser =
                optAppUser.orElseThrow(() -> new UsernameNotFoundException("User not found in the DB"));
        return appUser;
    }

    @Override
    public List<AppUser> getAllAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public void setAppUserActive(AppUser appUser) {
        appUser.setActive(true);
    }

    @Override
    public AppUser registerUser(RegisterRequestDTO request) throws MessagingException {

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
        newUser.setPassword(request.getPassword());

        String code = generateActivationCode();
        ActivationCode activationCode = new ActivationCode(code, newUser);

        AppUser savedUser = saveUser(newUser);
        activationCodeRepository.save(activationCode);

        activationCode.setAppUser(savedUser);

        emailService.registerConfirmationEmail(savedUser.getEmail(), savedUser.getUsername(), activationCode.getActivationCode());

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
