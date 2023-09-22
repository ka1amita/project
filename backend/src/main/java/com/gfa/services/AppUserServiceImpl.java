package com.gfa.services;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.*;
import com.gfa.exceptions.activation.ActivationCodeExpiredException;
import com.gfa.exceptions.activation.InvalidActivationCodeException;
import com.gfa.exceptions.email.EmailAlreadyExistsException;
import com.gfa.exceptions.email.EmailSendingFailedException;
import com.gfa.exceptions.user.*;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import com.gfa.repositories.RoleRepository;
import com.gfa.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final RoleService roleService;
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final ActivationCodeService activationCodeService;

    @Value("${ACTIVATION_CODE_EXPIRE_MINUTES:30}")
    private final Integer ActivationCodeExpireMinutes = 30;
    @Value("${ACTIVATION_CODE_MAX_SIZE:48}")
    private final Integer ActivationCodeMaxSize = 48;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              @Lazy BCryptPasswordEncoder bCryptPasswordEncoder,
                              EmailService emailService, ActivationCodeService activationCodeService, RoleService roleService) {

        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.activationCodeService = activationCodeService;
        this.roleService = roleService;
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
            ActivationCode activationCode = activationCodeService.saveActivationCode(new ActivationCode(Utils.GenerateActivationCode(ActivationCodeMaxSize), appUser.get()));
            emailService.resetPasswordEmail(appUser.get().getEmail(), appUser.get().getUsername(), activationCode.getActivationCode());
            return ResponseEntity.ok(new PasswordResetResponseDTO(activationCode.getActivationCode()));
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO, String resetCode) {
        Optional<ActivationCode> activationCode = activationCodeService.findByActivationCodeContains(resetCode);
        if (activationCode.isPresent() && activationCode.get().getCreatedAt().plusMinutes(ActivationCodeExpireMinutes).isAfter(LocalDateTime.now())) {
            if (Utils.IsUserPasswordFormatValid(passwordResetWithCodeRequestDTO.getPassword())) {
                AppUser appUser = activationCode.get().getAppUser();
                appUser.setPassword(passwordResetWithCodeRequestDTO.getPassword());
                saveUser(appUser);
                activationCodeService.deleteActivationCode(activationCode.get());
            } else {
                throw new InvalidPasswordFormatException("Password must contain at least 8 characters, including at least 1 lower case, 1 upper case, 1 number, and 1 special character.");
            }
        } else {
            throw new InvalidActivationCodeException("Reset code doesn't exist!");
        }
        return ResponseEntity.ok(new PasswordResetWithCodeResponseDTO("Password has been successfully changed."));
    }

    @Override
    public void addRoleToAppUser(String username, String roleName) {
        AppUser appUser =
                appUserRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                "Username not found in the DB"));
        Role role =
                roleService.findByName(roleName);
        appUser.getRoles()
                .add(role);
    }

    @Override
    public void addRoleToAppUser(AppUser appUser, String roleName) {
        Role role =
                roleService.findByName(roleName);
        appUser.getRoles()
                .add(role);
    }

    @Override
    public AppUser saveUser(AppUser appUser) {
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
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
    public void removeAppUser(Long id) {
        
    }

    @Override
    public AppUser registerUser(RegisterRequestDTO request) {

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new InvalidPasswordFormatException("Password cannot be null or empty");
        }

        if (!Utils.IsUserPasswordFormatValid(request.getPassword())) {
            throw new InvalidPasswordFormatException("Password must contain at least 8 characters, including at least 1 lower case, 1 upper case, 1 number, and 1 special character");
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());

        String code = Utils.GenerateActivationCode(ActivationCodeMaxSize);
        ActivationCode activationCode = new ActivationCode(code, newUser);

        AppUser savedUser = saveUser(newUser);
        activationCodeService.saveActivationCode(activationCode);

        activationCode.setAppUser(savedUser);

        try {
            emailService.registerConfirmationEmail(savedUser.getEmail(), savedUser.getUsername(), activationCode.getActivationCode());
        } catch (MessagingException e) {
            throw new EmailSendingFailedException("Unable to send the activation email");
        }

        return savedUser;
    }

    @Override
    public void activateAccount(String code) {
        Optional<ActivationCode> activationCodeOpt = activationCodeService.findByActivationCodeContains(code);

        if (!activationCodeOpt.isPresent()) {
            throw new InvalidActivationCodeException("Invalid activation code");
        }

        ActivationCode activationCode = activationCodeOpt.get();
        AppUser appUser = activationCode.getAppUser();

        LocalDateTime activationCodeCreationTime = activationCode.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = activationCodeCreationTime.plusMinutes(ActivationCodeExpireMinutes);

        if (now.isAfter(expirationTime)) {
            throw new ActivationCodeExpiredException("Activation code has expired");
        }

        appUser.setActive(true);
        appUserRepository.save(appUser);

        activationCodeService.deleteActivationCode(activationCode);
    }

}
