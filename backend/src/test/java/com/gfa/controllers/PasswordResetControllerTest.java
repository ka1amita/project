package com.gfa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.requestdtos.PasswordResetRequestDTO;
import com.gfa.dtos.requestdtos.PasswordResetWithCodeRequestDTO;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.repositories.ActivationCodeRepository;
import com.gfa.repositories.AppUserRepository;
import java.util.HashSet;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PasswordResetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    private ObjectMapper objectMapper;

    private final AppUser appUser = new AppUser("Will Doe", "1234", "example2@mail.com", new HashSet<>());
    private final ActivationCode activationCode = new ActivationCode("ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd", appUser);

    @BeforeAll
    void init() {
        appUserRepository.save(appUser);
        activationCodeRepository.save(activationCode);
    }

//    @AfterAll
//    void deInit() {
//        activationCodeRepository.delete(activationCode);
//        appUserRepository.delete(appUser);
//    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void Password_Reset_Request_Successful_With_Both_Username_And_Email() throws Exception {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("Will Doe", "example2@mail.com");
        mockMvc.perform(post("/reset")
                        .content(objectMapper.writeValueAsString(passwordResetRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniqueResetCode", hasToString(Matchers.hasLength(48))));
    }

    @Test
    void Password_Reset_Request_Successful_With_Username() throws Exception {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("Will Doe", null);
        mockMvc.perform(post("/reset")
                        .content(objectMapper.writeValueAsString(passwordResetRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniqueResetCode", hasToString(Matchers.hasLength(48))));
    }

    @Test
    void Password_Reset_Request_Successful_With_Email() throws Exception {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO(null, "example2@mail.com");
        mockMvc.perform(post("/reset")
                        .content(objectMapper.writeValueAsString(passwordResetRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uniqueResetCode", hasToString(Matchers.hasLength(48))));
    }

    @Test
    void Password_Reset_Request_Failed_With_No_Data() throws Exception {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO(null, null);
        mockMvc.perform(post("/reset")
                        .content(objectMapper.writeValueAsString(passwordResetRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Password_Reset_Request_Failed_With_Nothing() throws Exception {
        PasswordResetRequestDTO passwordResetRequestDTO = null;
        mockMvc.perform(post("/reset")
                        .content(objectMapper.writeValueAsString(passwordResetRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Password_Reset_Request_Failed_With_Empty_Strings() throws Exception {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("", "");
        mockMvc.perform(post("/reset")
                        .content(objectMapper.writeValueAsString(passwordResetRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Password_Reset_Request_Failed_With_Non_Existing_User() throws Exception {
        PasswordResetRequestDTO passwordResetRequestDTO = new PasswordResetRequestDTO("nonexistinguser123456789", null);
        mockMvc.perform(post("/reset")
                        .content(objectMapper.writeValueAsString(passwordResetRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void Password_Reset_With_Code_Request_Successful_With_Existing_Code() throws Exception {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("123456");
        mockMvc.perform(post("/reset/ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd")
                        .content(objectMapper.writeValueAsString(passwordResetWithCodeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void Password_Reset_With_Code_Request_Failed_With_Empty_Password() throws Exception {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("");
        mockMvc.perform(post("/reset/ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd")
                        .content(objectMapper.writeValueAsString(passwordResetWithCodeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Password_Reset_With_Code_Request_Failed_With_No_Password() throws Exception {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = null;
        mockMvc.perform(post("/reset/ctrauzhrdquulnctfhyrtiaztmrsnniwxggfoeurcbyctvhd")
                        .content(objectMapper.writeValueAsString(passwordResetWithCodeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void Password_Reset_With_Code_Request_Failed_With_Non_Existing_Code() throws Exception {
        PasswordResetWithCodeRequestDTO passwordResetWithCodeRequestDTO = new PasswordResetWithCodeRequestDTO("123456");
        mockMvc.perform(post("/reset/wrongcode")
                        .content(objectMapper.writeValueAsString(passwordResetWithCodeRequestDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}