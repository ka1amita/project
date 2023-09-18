package com.gfa.services;

import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.dtos.requestdtos.RegisterRequestDTO;
import com.gfa.dtos.responsedtos.PasswordResetResponseDTO;
import com.gfa.dtos.responsedtos.PasswordResetWithCodeResponseDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AppUserServiceTest {

    @Autowired
    private AppUserServiceImpl userService;

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private ActivationCodeRepository activationCodeRepository;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private EmailServiceImpl emailService;

    @Test
    public void register_user_successful() throws MessagingException {
        String testUsername = "testUser";
        String testEmail = "testEmail@example.com";
        String testPassword = "Valid@1234";

        when(appUserRepository.existsByUsername(testUsername)).thenReturn(false);
        when(appUserRepository.existsByEmail(testEmail)).thenReturn(false);

        AppUser savedUser = new AppUser();
        savedUser.setUsername(testUsername);
        savedUser.setEmail(testEmail);
        savedUser.setPassword(testPassword);

        when(appUserRepository.save(any())).thenReturn(savedUser);

        RegisterRequestDTO request = new RegisterRequestDTO(testUsername, testEmail, testPassword);
        AppUser returnedUser = userService.registerUser(request);

        assertNotNull(returnedUser);
        assertEquals(testUsername, returnedUser.getUsername());
        assertEquals(testEmail, returnedUser.getEmail());

        verify(emailService, times(1)).registerConfirmationEmail(eq(testEmail), eq(testUsername), anyString());
    }

    @Test
    public void register_user_existing_username() {
        when(appUserRepository.existsByUsername("testUser")).thenReturn(true);
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", "Valid@1234");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_existing_email() {
        when(appUserRepository.existsByEmail("testEmail@example.com")).thenReturn(true);
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "Valid@1234");
        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_null_password() {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_invalid_password_pattern() {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", "invalid");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_password_encryption() throws MessagingException {
        String testUsername = "testUser";
        String testEmail = "testEmail@example.com";
        String testPassword = "Valid@1234";
        String encryptedPassword = "encryptedPass";

        when(appUserRepository.existsByUsername(testUsername)).thenReturn(false);
        when(appUserRepository.existsByEmail(testEmail)).thenReturn(false);

        when(bCryptPasswordEncoder.encode(testPassword)).thenReturn(encryptedPassword);

        AppUser savedUser = new AppUser();
        savedUser.setUsername(testUsername);
        savedUser.setEmail(testEmail);
        savedUser.setPassword(encryptedPassword);

        when(appUserRepository.save(any())).thenReturn(savedUser);

        Authentication auth = mock(Authentication.class);
        doReturn(auth).when(authenticationManager).authenticate(Mockito.isA(UsernamePasswordAuthenticationToken.class));

        RegisterRequestDTO request = new RegisterRequestDTO(testUsername, testEmail, testPassword);
        AppUser returnedUser = userService.registerUser(request);

        assertNotNull(returnedUser);
        assertEquals(testUsername, returnedUser.getUsername());
        assertEquals(testEmail, returnedUser.getEmail());
        assertNotEquals(testPassword, returnedUser.getPassword());
        assertEquals(encryptedPassword, returnedUser.getPassword());
    }

    @Test
    public void activate_account_successful() {
        AppUser mockUser = new AppUser();
        mockUser.setActive(false);

        ActivationCode mockActivationCode = new ActivationCode();
        mockActivationCode.setActivationCode("validCode");
        mockActivationCode.setAppUser(mockUser);
        mockActivationCode.setCreatedAt(LocalDateTime.now().minusHours(10));

        when(activationCodeRepository.findByActivationCodeContains("validCode")).thenReturn(Optional.of(mockActivationCode));

        when(appUserRepository.save(any())).thenReturn(mockUser);

        userService.activateAccount("validCode");

        assertTrue(mockUser.isActive());

        verify(activationCodeRepository, times(1)).delete(mockActivationCode);
    }

    @Test
    public void activate_account_invalid_code() {
        when(activationCodeRepository.findByActivationCodeContains("invalidCode")).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidActivationCodeException.class, () -> userService.activateAccount("invalidCode"));
    }

}