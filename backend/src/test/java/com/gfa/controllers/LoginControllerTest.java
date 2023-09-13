package com.gfa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gfa.dtos.requestdtos.LoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@AutoConfigureMockMvc
@SpringBootTest
public class LoginControllerTest {

    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void beforeEachTest() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void Login_Successful_And_JWT_Token_Is_Generated() throws Exception {
        LoginRequestDto payload = new LoginRequestDto("John Doe", "1234");
        mockMvc.perform(
                        post("/login")
                                .content(objectMapper.writeValueAsString(payload))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.jwtToken", is("Demo token")));
    }

    @Test
    public void Login_Failed_Due_To_Empty_Or_Missing_JSON_Body() throws Exception {
        mockMvc.perform(
                        post("/login"))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.status", is("INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.message", is("Unable to submit post : Input body was not received.")));
    }

    @Test
    public void Login_Failed_Due_To_Missing_Username_Or_Email() throws Exception {
        LoginRequestDto payload = new LoginRequestDto("", "1234");
        mockMvc.perform(
                        post("/login")
                                .content(objectMapper.writeValueAsString(payload))
                                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().is(400))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.message", is("Unable to submit post : Please provide a name or an email address.")));
    }

    @Test
    public void Login_Failed_Due_To_Missing_Password() throws Exception {
        LoginRequestDto payload = new LoginRequestDto("John Doe", "");
        mockMvc.perform(
                        post("/login")
                                .content(objectMapper.writeValueAsString(payload))
                                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().is(400))
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.message", is("Unable to submit post : Please provide a password.")));
    }

    @Test
    public void Login_Failed_Due_To_User_Not_Existing_In_Database() throws Exception {
        LoginRequestDto payload = new LoginRequestDto("John ", "1234");
        mockMvc.perform(
                        post("/login")
                                .content(objectMapper.writeValueAsString(payload))
                                .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().is(500))
                .andExpect(jsonPath("$.status", is("INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.timeStamp", notNullValue()))
                .andExpect(jsonPath("$.message", is("Unable to submit post : The user can not be found in the database.")));
    }
}
