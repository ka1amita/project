package com.gfa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.models.ActivationCode;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import com.gfa.services.AppUserService;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class LoginControllerTest {

    ObjectMapper objectMapper;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void beforeEachTest() {
        objectMapper = new ObjectMapper();
        Role roleUser = new Role("ROLE_USER");
        AppUser user = new AppUser(1L,"user", "user", "user2@gfa.com", new HashSet<>());
        user.setActive(true);
        user.getRoles().add(roleUser);
        ActivationCode code = new ActivationCode("code", user);
        user.getActivationCodes().add(code);
        appUserService.saveUser(user);
    }

    @Test
    public void Successful_Login_Via_Login_Form() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "user")
                        .param("password", "user"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void Failed_Login_Due_To_Wrong_Username() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .param("username", "wrongUsername")
                .param("password","user"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void Failed_Login_Due_To_Wrong_Password() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "user")
                        .param("password","wrongPassword"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void Failed_Login_Due_To_Missing_Username() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "")
                        .param("password","user"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void Failed_Login_Due_To_Missing_Password() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "user")
                        .param("password",""))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
