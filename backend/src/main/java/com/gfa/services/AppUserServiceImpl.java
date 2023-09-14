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
import com.gfa.models.Role;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import com.gfa.repositories.RoleRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              ActivationCodeRepository activationCodeRepository,
                              RoleRepository roleRepository,
                              @Lazy PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        appUserRepository.save(appUser);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getUsername()));
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
    public AppUser setAppUserActive(AppUser appUser) {
        appUser.setActive(true);
        return appUserRepository.save(appUser);
    }
}
