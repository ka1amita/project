// package com.gfa.controllers;
//
// import com.gfa.dtos.requestdtos.RegisterRequestDTO;
// import com.gfa.exceptions.email.EmailAlreadyExistsException;
// import com.gfa.exceptions.activation.InvalidActivationCodeException;
// import com.gfa.exceptions.user.UserAlreadyExistsException;
// import com.gfa.services.AppUserService;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.gfa.services.EmailService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;
//
// import static org.hamcrest.Matchers.containsString;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// @SpringBootTest
// @AutoConfigureMockMvc
// public class RegistrationControllerTest {
//
//     @Autowired
//     private MockMvc mockMvc;
//
//     @MockBean
//     private AppUserService appUserService;
//
//     @MockBean
//     private EmailService emailService;
//
//     private ObjectMapper objectMapper = new ObjectMapper();
//
//     @BeforeEach
//     void setUp() {
//         reset(appUserService);
//     }
//
//     private ResultActions performPost(RegisterRequestDTO request) throws Exception {
//         String requestBody = objectMapper.writeValueAsString(request);
//         return mockMvc.perform(post("/register")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(requestBody));
//     }
//
//     @Test
//     public void registerUser_successful() throws Exception {
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "Valid@1234");
//         performPost(request)
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("{\"message\":\"Registration successful, please activate your account!\"}"));
//         verify(appUserService, times(1)).registerUser(any());
//     }
//
//     @Test
//     public void registerUser_usernameExists() throws Exception {
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "Valid@1234");
//         String requestBody = objectMapper.writeValueAsString(request);
//
//         when(appUserService.registerUser(any())).thenThrow(new UserAlreadyExistsException("Username already exists."));
//
//         mockMvc.perform(post("/register")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(requestBody))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().json("{\"errorMessage\":\"Username already exists.\"}"));
//     }
//
//     @Test
//     public void registerUser_emailExists() throws Exception {
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "Valid@1234");
//         String requestBody = objectMapper.writeValueAsString(request);
//
//         when(appUserService.registerUser(any())).thenThrow(new EmailAlreadyExistsException("Email already exists."));
//
//         mockMvc.perform(post("/register")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(requestBody))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().json("{\"errorMessage\":\"Email already exists.\"}"));
//     }
//
//     @Test
//     public void registerUser_withMissingName() throws Exception {
//         RegisterRequestDTO request = new RegisterRequestDTO(null, "testEmail@example.com", "Valid@1234");
//         performPost(request)
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().json("{\"errorMessage\":\"Username cannot be null or empty\"}"));
//
//     }
//
//     @Test
//     public void registerUser_withMissingEmail() throws Exception {
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", null, "Valid@1234");
//         performPost(request)
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().json("{\"errorMessage\":\"Email cannot be null or empty\"}"));
//
//     }
//
//     @Test
//     public void registerUser_withMissingPassword() throws Exception {
//         RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", null);
//         performPost(request)
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().json("{\"errorMessage\":\"Password cannot be null or empty\"}"));
//     }
//
//     @Test
//     public void registerUser_withAllFieldsMissing() throws Exception {
//         RegisterRequestDTO request = new RegisterRequestDTO(null, null, null);
//         performPost(request)
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string(containsString("Email cannot be null or empty")))
//                 .andExpect(content().string(containsString("Password cannot be null or empty")))
//                 .andExpect(content().string(containsString("Username cannot be null or empty")));
//     }
//
//     @Test
//     public void activateAccount_successful() throws Exception {
//         String activationCode = "validCode";
//         doNothing().when(appUserService).activateAccount(activationCode);
//
//         mockMvc.perform(get("/confirm/{activationCode}", activationCode)
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk());
//     }
//
//     @Test
//     public void activateAccount_invalidCode() throws Exception {
//         doThrow(new InvalidActivationCodeException("Invalid activation code."))
//                 .when(appUserService).activateAccount("invalidActivationCode");
//
//         mockMvc.perform(get("/confirm/invalidActivationCode"))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().json("{\"errorMessage\":\"Invalid activation code.\"}"));
//     }
//
//     @Test
//     public void activateAccount_withoutActivationCode() throws Exception {
//         mockMvc.perform(get("/confirm/"))
//                 .andExpect(status().isNotFound());
//     }
// }
