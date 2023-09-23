package com.gfa.services;

import com.gfa.config.SoftDeleteConfig;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.AppUserResponseDTO;
import com.gfa.dtos.responsedtos.PasswordResetResponseDTO;
import com.gfa.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.exceptions.activation.ActivationCodeExpiredException;
import com.gfa.exceptions.activation.InvalidActivationCodeException;
import com.gfa.exceptions.email.EmailAlreadyExistsException;
import com.gfa.exceptions.email.EmailSendingFailedException;
import com.gfa.exceptions.user.*;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import com.gfa.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ActivationCodeService activationCodeService;
    private final RoleService roleService;

    @Value("${ACTIVATION_CODE_EXPIRE_MINUTES:30}")
    private final Integer ActivationCodeExpireMinutes = 30;
    @Value("${ACTIVATION_CODE_MAX_SIZE:48}")
    private final Integer ActivationCodeMaxSize = 48;
    private final SoftDeleteConfig softDeleteConfig;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              PasswordEncoder passwordEncoder,
                              EmailService emailService, ActivationCodeService activationCodeService, RoleService roleService, SoftDeleteConfig softDeleteConfig) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.activationCodeService = activationCodeService;
        this.roleService = roleService;
        this.softDeleteConfig = softDeleteConfig;
    }

    @Override
    public ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO) throws MessagingException {
        Optional<AppUser> appUser = Optional.empty();
        if (passwordResetRequestDTO != null) {
            appUser = appUserRepository.findByEmail(passwordResetRequestDTO.getUsernameOrEmail());
            if (!appUser.isPresent()) {
                appUser = appUserRepository.findByUsername(passwordResetRequestDTO.getUsernameOrEmail());
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
                encodePasswordAndSaveAppUser(appUser);
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
    public AppUser encodePasswordAndSaveAppUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    /**
     * use findByUsernameOrEmail
     */
    @Override
    public AppUser findUserByUsername(String username) {
        Optional<AppUser> optAppUser = appUserRepository.findByUsername(username);
        return optAppUser.orElseThrow(
            () -> new UsernameNotFoundException("User not found in the DB"));
    }

    @Override
    public List<AppUser> getAllAppUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public void removeAppUser(Long id) {
        if (id < 0) throw new InvalidIdException("Please provide a valid ID");
        AppUser user =
                appUserRepository.findById(id).orElseThrow(()
                        -> new UserNotFoundException("User not found")
                );
        if (softDeleteConfig.isEnabled()) {
            user.setDeleted(true);
            user.setActive(false);
            appUserRepository.save(user);
        } else {
            appUserRepository.deleteById(id);
        }
    }

    @Override
    public void setAppUserActive(AppUser appUser) {
        appUser.setActive(true);
    }

    @Override
    public AppUser findByUsernameOrEmail(String username) {
        return appUserRepository.findByUsernameOrEmail(username, username)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                    "User not found in the DB"));
    }

    @Override
    public Page<AppUserResponseDTO> pageAppUserDtos(PageRequest pageRequest) {
        Page<AppUser> page = pageAppUsers(pageRequest);
        Page<AppUserResponseDTO> dtos = new PageImpl<>(null);
        return dtos;
    }
    public Page<AppUser> pageAppUsers(PageRequest pageRequest) {
        Page<AppUser> page = appUserRepository.findAll(pageRequest);
        return page;
    }

    @Override
    public AppUser registerUser(RegisterRequestDTO request) throws MessagingException {

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
        newUser.assignRole(roleService.findByName("USER"));
        newUser.setCreated_at(LocalDateTime.now());

        String code = Utils.GenerateActivationCode(ActivationCodeMaxSize);
        ActivationCode activationCode = new ActivationCode(code, newUser);

        encodePasswordAndSaveAppUser(newUser);
        activationCodeService.saveActivationCode(activationCode);

        activationCode.setAppUser(newUser);

        try {
            emailService.registerConfirmationEmail(newUser.getEmail(), newUser.getUsername(), activationCode.getActivationCode());
        } catch (MessagingException e) {
            throw new EmailSendingFailedException("Unable to send the activation email");
        }

        return newUser;
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
        appUser.setVerified_at(LocalDateTime.now());
        appUserRepository.save(appUser);

        activationCodeService.deleteActivationCode(activationCode);
    }
}
