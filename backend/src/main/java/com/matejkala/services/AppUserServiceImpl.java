package com.matejkala.services;

import com.matejkala.config.SoftDeleteConfig;
import com.matejkala.dtos.requestdtos.PasswordResetRequestDTO;
import com.matejkala.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.matejkala.dtos.requestdtos.RegisterRequestDTO;
import com.matejkala.dtos.responsedtos.PasswordResetResponseDTO;
import com.matejkala.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.matejkala.dtos.responsedtos.ResponseDTO;
import com.matejkala.exceptions.activation.ActivationCodeExpiredException;
import com.matejkala.exceptions.activation.InvalidActivationCodeException;
import com.matejkala.exceptions.email.EmailAlreadyExistsException;
import com.matejkala.exceptions.user.*;
import com.matejkala.exceptions.email.EmailFormatException;
import com.matejkala.models.ActivationCode;
import com.matejkala.models.AppUser;
import com.matejkala.models.Role;
import com.matejkala.repositories.AppUserRepository;
import com.matejkala.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Locale;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.matejkala.dtos.responsedtos.AppUserResponseDTO;
import com.matejkala.dtos.requestdtos.UpdateAppUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.mail.MessagingException;

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
    private final MessageSource messageSource;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              @Lazy PasswordEncoder passwordEncoder,
                              EmailService emailService,
                              ActivationCodeService activationCodeService,
                              RoleService roleService,
                              SoftDeleteConfig softDeleteConfig,
                              MessageSource messageSource) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.activationCodeService = activationCodeService;
        this.roleService = roleService;
        this.softDeleteConfig = softDeleteConfig;
        this.messageSource = messageSource;
    }

    @Override
    public ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO) throws
            MessagingException {
        Optional<AppUser> appUser = Optional.empty();
        if (passwordResetRequestDTO != null) {
            appUser = appUserRepository.findByEmail(passwordResetRequestDTO.getUsernameOrEmail());
            if (!appUser.isPresent()) {
                appUser = appUserRepository.findByUsername(passwordResetRequestDTO.getUsernameOrEmail());
            }
        }

        if (appUser.isPresent()) {
            ActivationCode activationCode = activationCodeService.saveActivationCode(new ActivationCode(Utils.GenerateActivationCode(ActivationCodeMaxSize), appUser.get()));
            emailService.resetPasswordEmail(appUser.get().getEmail(), appUser.get().getUsername(), activationCode.getActivationCode(), passwordResetRequestDTO.getRedirectUrl());
            return ResponseEntity.ok(new PasswordResetResponseDTO(activationCode.getActivationCode()));
        } else {
            throw new UserNotFoundException(messageSource.getMessage("error.user.not.found", null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO
                                                             passwordResetWithCodeRequestDTO, String resetCode) {
        Optional<ActivationCode> activationCode = activationCodeService.findByActivationCodeContains(resetCode);
        if (activationCode.isPresent() && activationCode.get().getCreatedAt().plusMinutes(ActivationCodeExpireMinutes).isAfter(LocalDateTime.now())) {
            if (Utils.IsUserPasswordFormatValid(passwordResetWithCodeRequestDTO.getPassword())) {
                AppUser appUser = activationCode.get().getAppUser();
                appUser.setPassword(passwordResetWithCodeRequestDTO.getPassword());
                saveUser(appUser);
                activationCodeService.deleteActivationCode(activationCode.get());
            } else {
                throw new InvalidPasswordFormatException(messageSource.getMessage("error.invalid.password.format", null, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new InvalidActivationCodeException(messageSource.getMessage("error.invalid.reset.code", null, LocaleContextHolder.getLocale()));
        }
        return ResponseEntity.ok(new PasswordResetWithCodeResponseDTO(messageSource.getMessage("dto.password.reset", null, LocaleContextHolder.getLocale())));
    }

    @Override
    public void addRoleToAppUser(String username, String roleName) {
        AppUser appUser =
                appUserRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                messageSource.getMessage("error.username.not.found", null, LocaleContextHolder.getLocale())));
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
     * use encodePasswordAndSaveAppUser()
     */
    @Deprecated
    public AppUser saveUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser findUserByUsername(String username) {
        Optional<AppUser> optAppUser = appUserRepository.findByUsername(username);
        return optAppUser.orElseThrow(
                () -> new UsernameNotFoundException(messageSource.getMessage("error.username.not.found", null, LocaleContextHolder.getLocale())));
    }

    /**
     * use findByUsername()
     */
    @Deprecated
    public AppUser getAppUser(String username) {
        Optional<AppUser> optAppUser = appUserRepository.findByUsername(username);
        return optAppUser.orElseThrow(
                () -> new UsernameNotFoundException(messageSource.getMessage("error.username.not.found", null, LocaleContextHolder.getLocale())));
    }

    @Override
    public AppUser fetchAppUserById(Long id) {
        return appUserRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(messageSource.getMessage("error.user.not.found", null, LocaleContextHolder.getLocale()))
        );
    }

    @Override
    public AppUserResponseDTO fetchUserApi(Long id) {
        if (id < 0) throw new InvalidIdException(messageSource.getMessage("error.invalid.id", null, LocaleContextHolder.getLocale()));
        AppUser user = fetchAppUserById(id);
        return new AppUserResponseDTO(user);
    }

    @Override
    public AppUserResponseDTO updateAppUserApi(Long id, UpdateAppUserDTO request) throws MessagingException {
        isIdValid(id);
        isJSONBodyPresent(request);
        AppUser appUser = fetchAppUserById(id);

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            appUser.setUsername(request.getUsername());
        }

        String oldEmail = appUser.getEmail();
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            appUser.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            if (Utils.IsUserPasswordFormatValid(request.getPassword())) {
                appUser.setPassword(request.getPassword());
            } else
                throw new InvalidPasswordFormatException(messageSource.getMessage("error.invalid.password.format", null, LocaleContextHolder.getLocale()));
        }
        if (!oldEmail.equals(appUser.getEmail())) {
            appUser.setActive(false);
            ActivationCode activationCode = assignActivationCodeToUser(appUser);
            emailService.registerConfirmationEmail(appUser.getEmail(), appUser.getUsername(), activationCode.getActivationCode());
        }
        return new AppUserResponseDTO(appUser);
    }

    public List<AppUserResponseDTO> getAllAppUsers() {
        return appUserRepository.findAll().stream()
                .map(AppUserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AppUserResponseDTO> pageDeletedAppUserDtos(PageRequest request) {
        return appUserRepository.findAllDeletedAppUsers(request).map(AppUserResponseDTO::new);
    }

    @Override
    public void removeAppUser(Long id) {
        isIdValid(id);
        AppUser user = appUserRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(messageSource.getMessage("error.user.not.found", null, LocaleContextHolder.getLocale())));
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
    public Optional<AppUser> findAppUserByActivationCode(String activationCode) {
        return Optional.empty();
    }

    @Override
    public AppUser findByUsernameOrEmail(String login) {
        Optional<AppUser> optAppUser = appUserRepository.findByUsernameOrEmail(login, login);
        return optAppUser.orElseThrow(
                () -> new UsernameNotFoundException(messageSource.getMessage("error.username.not.found", null, LocaleContextHolder.getLocale())));
    }

    @Override
    public Page<AppUserResponseDTO> pageAppUserDtos(PageRequest request) {
        return appUserRepository.findAll(request).map(AppUserResponseDTO::new);

    }

    @Override
    public AppUser registerUser(RegisterRequestDTO request) throws MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();
        List<AppUser> deletedUsers = appUserRepository.findAllDeletedAppUsersList();

        for(AppUser user : deletedUsers) {
            if(user.getUsername().equals(request.getUsername())) {
                throw new UserAlreadyExistsException(messageSource.getMessage("error.username.exists", null, currentLocale));            
            }
        }

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(messageSource.getMessage("error.username.exists", null, LocaleContextHolder.getLocale()));
        }

        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(messageSource.getMessage("error.email.exists", null, LocaleContextHolder.getLocale()));
        }

        if (!request.getEmail().contains("@")) {
            throw new EmailFormatException(messageSource.getMessage("error.email.missing.at.sign", null, LocaleContextHolder.getLocale()));
        }

        if (!Utils.IsUserPasswordFormatValid(request.getPassword())) {
            throw new InvalidPasswordFormatException(messageSource.getMessage("error.invalid.password.format", null, LocaleContextHolder.getLocale()));
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.assignRole(roleService.findByName("USER"));
        newUser.setCreatedAt(LocalDateTime.now());
        encodePasswordAndSaveAppUser(newUser);

        ActivationCode activationCode = assignActivationCodeToUser(newUser);

        if(Utils.isNotNullOrEmpty(request.getRedirectUrl())) {
            emailService.registerConfirmationEmail(newUser.getEmail(), newUser.getUsername(), activationCode.getActivationCode(), request.getRedirectUrl());
        } else {
            emailService.registerConfirmationEmail(newUser.getEmail(), newUser.getUsername(), activationCode.getActivationCode());
        }


        return newUser;
    }

    @Override
    public void activateAccount(String code) {
        Optional<ActivationCode> activationCodeOpt = activationCodeService.findByActivationCodeContains(code);

        if (!activationCodeOpt.isPresent()) {
            throw new InvalidActivationCodeException(messageSource.getMessage("error.invalid.activation.code", null, LocaleContextHolder.getLocale()));
        }

        ActivationCode activationCode = activationCodeOpt.get();
        AppUser appUser = activationCode.getAppUser();

        LocalDateTime activationCodeCreationTime = activationCode.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = activationCodeCreationTime.plusMinutes(ActivationCodeExpireMinutes);

        if (now.isAfter(expirationTime)) {
            throw new ActivationCodeExpiredException(messageSource.getMessage("error.activation.code.expiry", null, LocaleContextHolder.getLocale()));
        }

        appUser.setActive(true);
        appUser.setVerifiedAt(now);
        appUserRepository.save(appUser);
        activationCodeService.deleteActivationCode(activationCode);
    }

    private ActivationCode assignActivationCodeToUser(AppUser appUser) {
        ActivationCode code = new ActivationCode(Utils.GenerateActivationCode(48), appUser);
        activationCodeService.saveActivationCode(code);
        return code;
    }

    private void isIdValid(Long id) {
        if (id < 0)
            throw new InvalidIdException(messageSource.getMessage("error.invalid.id", null, LocaleContextHolder.getLocale()));
    }

    public <T> void isJSONBodyPresent(T request) {
        if (request == null)
            throw new MissingJSONBodyException(messageSource.getMessage("error.missing.json.body", null, LocaleContextHolder.getLocale()));
    }
}