package com.gfa.contollers;

import com.gfa.dtos.responsedtos.RegisterRequestDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import com.gfa.services.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        reset(appUserService);
    }

    @Test
    public void registerUser_successful() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "S@ck4Dic");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"Registration successful, please activate your account!\"}"));
    }

    @Test
    public void registerUser_usernameExists() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "S@ck4Dic");
        String requestBody = objectMapper.writeValueAsString(request);

        when(appUserService.registerUser(any())).thenThrow(new UserAlreadyExistsException("Username already exists."));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Username already exists.\"}"));


    }

    @Test
    public void registerUser_emailExists() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", "S@ck4Dic");
        String requestBody = objectMapper.writeValueAsString(request);

        when(appUserService.registerUser(any())).thenThrow(new EmailAlreadyExistsException("Email already exists."));

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Email already exists.\"}"));

    }

    @Test
    public void registerUser_withMissingName() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO(null, "testEmail@example.com", "S@ck4Dic");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Username cannot be null or empty\"}"));

    }

    @Test
    public void registerUser_withMissingEmail() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", null, "S@ck4Dic");
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Email cannot be null or empty\"}"));

    }

    @Test
    public void registerUser_withMissingPassword() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO("testUser", "testEmail@example.com", null);
        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Password cannot be null or empty\"}"));
    }

    @Test
    public void activateAccount_successful() throws Exception {
        String activationCode = "validCode";
        doNothing().when(appUserService).activateAccount(activationCode);

        mockMvc.perform(get("/confirm/{activationCode}", activationCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void activateAccount_invalidCode() throws Exception {
        doThrow(new InvalidActivationCodeException("Invalid activation code."))
                .when(appUserService).activateAccount("invalidActivationCode");

        mockMvc.perform(get("/confirm/invalidActivationCode"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errorMessage\":\"Invalid activation code.\"}"));
    }

    @Test
    public void activateAccount_withoutActivationCode() throws Exception {
        mockMvc.perform(get("/confirm/"))
                .andExpect(status().isNotFound());
    }
}
