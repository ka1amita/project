package com.gfa.Services;

import com.gfa.dtos.responsedtos.RegisterRequestDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.UserRepository;
import com.gfa.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ActivationCodeRepository activationCodeRepository;

    @Test
    public void register_user_successful() {
        String testUsername = "testUser";
        String testEmail = "testEmail@example.com";
        String testPassword = "S@ck4Dic";

        when(userRepository.existsByUsername(testUsername)).thenReturn(false);
        when(userRepository.existsByEmail(testEmail)).thenReturn(false);

        AppUser savedUser = new AppUser();
        savedUser.setUsername(testUsername);
        savedUser.setEmail(testEmail);
        savedUser.setPassword(testPassword);

        when(userRepository.save(any())).thenReturn(savedUser);

        RegisterRequestDTO request = new RegisterRequestDTO(testUsername, testEmail, testPassword);
        AppUser returnedUser = userService.registerUser(request);

        assertNotNull(returnedUser);
        assertEquals(testUsername, returnedUser.getUsername());
        assertEquals(testEmail, returnedUser.getEmail());
    }


    @Test
    public void register_user_null_username() {
        RegisterRequestDTO request = new RegisterRequestDTO(null, "testEmail@mail.com", "S@ck4D");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_existing_username() {
        when(userRepository.existsByUsername("testUser")).thenReturn(true);
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", "S@ck4D");
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_null_email() {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", null, "S@ck4D");
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
    }

    @Test
    public void register_user_existing_email() {
        when(userRepository.existsByEmail("testEmail@example.com")).thenReturn(true);
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "S@ck4D");
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
    public void activateAccount_successful() {
        AppUser mockUser = new AppUser();
        mockUser.setActive(false);

        ActivationCode mockActivationCode = new ActivationCode();
        mockActivationCode.setActivationCode("validCode");
        mockActivationCode.setAppUser(mockUser);
        mockActivationCode.setCreatedAt(LocalDateTime.now().minusHours(10));

        when(activationCodeRepository.findByActivationCode("validCode")).thenReturn(Optional.of(mockActivationCode));

        when(userRepository.save(any(AppUser.class))).thenAnswer(invocation -> {
            AppUser savedUser = invocation.getArgument(0);
            mockUser.setActive(savedUser.isActive());
            return savedUser;
        });


        userService.activateAccount("validCode");

        assertTrue(mockUser.isActive());

        when(activationCodeRepository.findByActivationCode("validCode")).thenReturn(Optional.empty());
        Optional<ActivationCode> retrievedCode = activationCodeRepository.findByActivationCode("validCode");
        assertFalse(retrievedCode.isPresent());
    }


    @Test
    public void activate_account_invalid_code() {
        when(activationCodeRepository.findByActivationCode("invalidCode")).thenReturn(Optional.empty());
        Assertions.assertThrows(InvalidActivationCodeException.class, () -> userService.activateAccount("invalidCode"));
    }

}
