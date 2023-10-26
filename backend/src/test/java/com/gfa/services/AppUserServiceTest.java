// package com.gfa.services;
//
// import com.gfa.dtos.requestdtos.RegisterRequestDTO;
// import com.gfa.exceptions.email.EmailAlreadyExistsException;
// import com.gfa.exceptions.activation.InvalidActivationCodeException;
// import com.gfa.exceptions.user.UserAlreadyExistsException;
// import com.gfa.models.ActivationCode;
// import com.gfa.models.AppUser;
// import com.gfa.repositories.ActivationCodeRepository;
// import com.gfa.repositories.AppUserRepository;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.springframework.security.core.Authentication;
// import org.mockito.Mockito;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
//
// import javax.mail.MessagingException;
// import java.time.LocalDateTime;
// import java.util.Optional;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;
//
//
// @ExtendWith(SpringExtension.class)
// @SpringBootTest
// public class AppUserServiceTest {
//
//     @Autowired
//     private AppUserServiceImpl userService;
//
//     @MockBean
//     private AppUserRepository appUserRepository;
//
//     @MockBean
//     private ActivationCodeRepository activationCodeRepository;
//
//     @MockBean
//     private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//     @MockBean
//     private AuthenticationManager authenticationManager;
//
//     @MockBean
//     private EmailServiceImpl emailService;
//
//     private AppUser mockUser;
//     private ActivationCode mockActivationCode;
//     private final String testUsername = "testUser";
//     private final String testEmail = "testEmail@example.com";
//     private final String testPassword = "Valid@1234";
//
//     @BeforeEach
//     public void setUp() {
//         mockUser = new AppUser();
//         mockUser.setUsername(testUsername);
//         mockUser.setEmail(testEmail);
//         mockUser.setPassword(testPassword);
//         mockUser.setActive(false);
//
//         mockActivationCode = new ActivationCode();
//         mockActivationCode.setActivationCode("validCode");
//         mockActivationCode.setAppUser(mockUser);
//         mockActivationCode.setCreatedAt(LocalDateTime.now().minusHours(10));
//     }
//     @Test
//     public void register_user_successful() throws MessagingException {
//         when(appUserRepository.existsByUsername(testUsername)).thenReturn(false);
//         when(appUserRepository.existsByEmail(testEmail)).thenReturn(false);
//         when(appUserRepository.save(any())).thenReturn(mockUser);
//
//         RegisterRequestDTO request = new RegisterRequestDTO(testUsername, testEmail, testPassword);
//         AppUser returnedUser = userService.registerUser(request);
//
//         assertNotNull(returnedUser);
//         assertEquals(testUsername, returnedUser.getUsername());
//         assertEquals(testEmail, returnedUser.getEmail());
//
//         verify(emailService, times(1)).registerConfirmationEmail(eq(testEmail), eq(testUsername), anyString());
//     }
//
//     @Test
//     public void register_user_existing_username() {
//         when(appUserRepository.existsByUsername("testUser")).thenReturn(true);
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", "Valid@1234");
//         Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(request));
//     }
//
//     @Test
//     public void register_user_existing_email() {
//         when(appUserRepository.existsByEmail("testEmail@example.com")).thenReturn(true);
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "Valid@1234");
//         Assertions.assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(request));
//     }
//
//     @Test
//     public void register_user_null_password() {
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", null);
//         Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
//     }
//
//     @Test
//     public void register_user_invalid_password_pattern() {
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@mail.com", "invalid");
//         Assertions.assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
//     }
//
//     @Test
//     public void register_user_password_encryption() throws MessagingException {
//         String encryptedPassword = "encryptedPass";
//
//         when(appUserRepository.existsByUsername(testUsername)).thenReturn(false);
//         when(appUserRepository.existsByEmail(testEmail)).thenReturn(false);
//         when(bCryptPasswordEncoder.encode(testPassword)).thenReturn(encryptedPassword);
//
//         mockUser.setPassword(encryptedPassword);
//         when(appUserRepository.save(any())).thenReturn(mockUser);
//
//         Authentication auth = mock(Authentication.class);
//         doReturn(auth).when(authenticationManager).authenticate(Mockito.isA(UsernamePasswordAuthenticationToken.class));
//
//         RegisterRequestDTO request = new RegisterRequestDTO(testUsername, testEmail, testPassword);
//         AppUser returnedUser = userService.registerUser(request);
//
//         assertNotNull(returnedUser);
//         assertEquals(testUsername, returnedUser.getUsername());
//         assertEquals(testEmail, returnedUser.getEmail());
//         assertNotEquals(testPassword, returnedUser.getPassword());
//         assertEquals(encryptedPassword, returnedUser.getPassword());
//     }
//
//     @Test
//     public void activate_account_successful() {
//         when(activationCodeRepository.findByActivationCode("validCode")).thenReturn(Optional.of(mockActivationCode));
//         when(appUserRepository.save(any())).thenReturn(mockUser);
//
//         userService.activateAccount("validCode");
//
//         assertTrue(mockUser.isActive());
//         verify(activationCodeRepository, times(1)).delete(mockActivationCode);
//     }
//
//     @Test
//     public void activate_account_invalid_code() {
//         when(activationCodeRepository.findByActivationCode("invalidCode")).thenReturn(Optional.empty());
//         Assertions.assertThrows(InvalidActivationCodeException.class, () -> userService.activateAccount("invalidCode"));
//     }
//
//     @Test
//     public void activate_account_already_active(){
//         mockUser.setActive(true);
//         when(activationCodeRepository.findByActivationCode("validCode")).thenReturn(Optional.of(mockActivationCode));
//         Assertions.assertThrows(IllegalStateException.class, () -> userService.activateAccount("validCode"));
//     }
//
//     @Test
//     public void activate_account_expired_code() {
//         mockActivationCode.setCreatedAt(LocalDateTime.now().minusDays(2));
//         when(activationCodeRepository.findByActivationCode("validCode")).thenReturn(Optional.of(mockActivationCode));
//         Assertions.assertThrows(IllegalStateException.class, () -> userService.activateAccount("validCode"));
//     }
// }