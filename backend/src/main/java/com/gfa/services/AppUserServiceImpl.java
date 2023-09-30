package com.gfa.services;

import com.gfa.config.SoftDeleteConfig;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.PasswordResetResponseDTO;
import com.gfa.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.gfa.dtos.responsedtos.ResponseDTO;
import com.gfa.exceptions.activation.ActivationCodeExpiredException;
import com.gfa.exceptions.activation.InvalidActivationCodeException;
import com.gfa.exceptions.email.EmailAlreadyExistsException;
import com.gfa.exceptions.user.*;
import com.gfa.exceptions.email.EmailFormatException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.repositories.AppUserRepository;
import com.gfa.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Locale;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.gfa.dtos.responsedtos.AppUserResponseDTO;
import com.gfa.dtos.requestdtos.UpdateAppUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final ActivationCodeService activationCodeService;
    private final RoleService roleService;

    @Value("${ACTIVATION_CODE_EXPIRE_MINUTES:30}")
    private final Integer ActivationCodeExpireMinutes = 30;
    @Value("${ACTIVATION_CODE_MAX_SIZE:48}")
    private final Integer ActivationCodeMaxSize = 48;
    private final SoftDeleteConfig softDeleteConfig;
    private final HttpServletRequest httpServletRequest;
    private final MessageSource messageSource;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              EmailService emailService,
                              ActivationCodeService activationCodeService,
                              RoleService roleService,
                              SoftDeleteConfig softDeleteConfig,
                              HttpServletRequest httpServletRequest,
                              MessageSource messageSource) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.activationCodeService = activationCodeService;
        this.roleService = roleService;
        this.softDeleteConfig = softDeleteConfig;
        this.httpServletRequest = httpServletRequest;
        this.messageSource = messageSource;
    }

    @Override
    public ResponseEntity<ResponseDTO> reset(PasswordResetRequestDTO passwordResetRequestDTO) throws
            MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();
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
            throw new UserNotFoundException(messageSource.getMessage("error.user.not.found", null, currentLocale));
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> resetWithCode(PasswordResetWithCodeRequestDTO
                                                             passwordResetWithCodeRequestDTO, String resetCode) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        Optional<ActivationCode> activationCode = activationCodeService.findByActivationCodeContains(resetCode);
        if (activationCode.isPresent() && activationCode.get().getCreatedAt().plusMinutes(ActivationCodeExpireMinutes).isAfter(LocalDateTime.now())) {
            if (Utils.IsUserPasswordFormatValid(passwordResetWithCodeRequestDTO.getPassword())) {
                AppUser appUser = activationCode.get().getAppUser();
                appUser.setPassword(passwordResetWithCodeRequestDTO.getPassword());
                saveUser(appUser);
                activationCodeService.deleteActivationCode(activationCode.get());
            } else {
                throw new InvalidPasswordFormatException(messageSource.getMessage("error.invalid.password.format", null, currentLocale));
            }
        } else {
            throw new InvalidActivationCodeException(messageSource.getMessage("error.invalid.reset.code", null, currentLocale));
        }
        return ResponseEntity.ok(new PasswordResetWithCodeResponseDTO(messageSource.getMessage("dto.password.reset", null, currentLocale)));
    }

    @Override
    public void addRoleToAppUser(String username, String roleName) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        AppUser appUser =
                appUserRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                messageSource.getMessage("error.username.not.found", null, currentLocale)));
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
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    /**
     * use encodePasswordAndSaveAppUser()
     */
    @Deprecated
    public AppUser saveUser(AppUser appUser) {
        appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser findUserByUsername(String username) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        Optional<AppUser> optAppUser = appUserRepository.findByUsername(username);
        return optAppUser.orElseThrow(
                () -> new UsernameNotFoundException(messageSource.getMessage("error.username.not.found", null, currentLocale)));
    }

    /**
     * use findByUsername()
     */
    @Deprecated
    public AppUser getAppUser(String username) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        Optional<AppUser> optAppUser = appUserRepository.findByUsername(username);
        return optAppUser.orElseThrow(
                () -> new UsernameNotFoundException(messageSource.getMessage("error.username.not.found", null, currentLocale)));
    }

    @Override
    public AppUser fetchAppUserById(Long id) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return appUserRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(messageSource.getMessage("error.user.not.found", null, currentLocale))
        );
    }

    @Override
    public AppUserResponseDTO fetchUserApi(Long id) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        if (id < 0) throw new InvalidIdException(messageSource.getMessage("error.invalid.id", null, currentLocale));
        AppUser user = fetchAppUserById(id);
        return new AppUserResponseDTO(user);
    }

    @Override
    public AppUserResponseDTO updateAppUserApi(Long id, UpdateAppUserDTO request) throws MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();
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
                throw new InvalidPasswordFormatException(messageSource.getMessage("error.invalid.password.format", null, currentLocale));
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
        Locale currentLocale = LocaleContextHolder.getLocale();
        isIdValid(id);
        AppUser user = appUserRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException(messageSource.getMessage("error.user.not.found", null, currentLocale)));
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
        Locale currentLocale = LocaleContextHolder.getLocale();
        Optional<AppUser> optAppUser = appUserRepository.findByUsernameOrEmail(login, login);
        return optAppUser.orElseThrow(
                () -> new UsernameNotFoundException(messageSource.getMessage("error.username.not.found", null, currentLocale)));
    }

    @Override
    public Page<AppUserResponseDTO> pageAppUserDtos(PageRequest request) {
        return appUserRepository.findAll(request).map(AppUserResponseDTO::new);

    }

    @Override
    public AppUser registerUser(RegisterRequestDTO request) throws MessagingException {
        Locale currentLocale = LocaleContextHolder.getLocale();

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(messageSource.getMessage("error.username.exists", null, currentLocale));
        }

        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(messageSource.getMessage("error.email.exists", null, currentLocale));
        }

        if (!request.getEmail().contains("@")) {
            throw new EmailFormatException(messageSource.getMessage("error.email.missing.at.sign", null, currentLocale));
        }

        if (!Utils.IsUserPasswordFormatValid(request.getPassword())) {
            throw new InvalidPasswordFormatException(messageSource.getMessage("error.invalid.password.format", null, currentLocale));
        }

        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.assignRole(roleService.findByName("USER"));
        newUser.setCreatedAt(LocalDateTime.now());

        //add
        String langHeader = httpServletRequest.getHeader("Accept-Language");
        if (langHeader != null && (langHeader.contains("en") || langHeader.contains("hu") || langHeader.contains("cz"))) {
            newUser.setPreferredLanguage(langHeader);  // saves "en", "hu" or "cz" in the database
        }

        ActivationCode activationCode = assignActivationCodeToUser(newUser);

        emailService.registerConfirmationEmail(newUser.getEmail(), newUser.getUsername(), activationCode.getActivationCode());

        return newUser;
    }

    @Override
    public String activateAccount(String code) {
        Optional<ActivationCode> activationCodeOpt = activationCodeService.findByActivationCodeContains(code);
        Locale currentLocale = LocaleContextHolder.getLocale();

        if (!activationCodeOpt.isPresent()) {
            throw new InvalidActivationCodeException(messageSource.getMessage("error.invalid.activation.code", null, currentLocale));
        }

        ActivationCode activationCode = activationCodeOpt.get();
        AppUser appUser = activationCode.getAppUser();
        String language = appUser.getPreferredLanguage();

        LocalDateTime activationCodeCreationTime = activationCode.getCreatedAt();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = activationCodeCreationTime.plusMinutes(ActivationCodeExpireMinutes);

        if (now.isAfter(expirationTime)) {
            throw new ActivationCodeExpiredException(messageSource.getMessage("error.activation.code.expiry", null, new Locale(language)));
        }

        appUser.setActive(true);
        appUser.setVerifiedAt(LocalDateTime.now());
        appUserRepository.save(appUser);
        String lang = appUser.getPreferredLanguage();
        activationCodeService.deleteActivationCode(activationCode);

        return lang;
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