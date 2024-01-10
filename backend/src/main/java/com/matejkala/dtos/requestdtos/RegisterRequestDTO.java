package com.matejkala.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


public class RegisterRequestDTO {
    @NotEmpty(message = "{error.username.empty.or.null}")
    private String username;
    @NotEmpty(message = "{error.email.empty.or.null}")
    @Email(message = "{error.email.invalid.format}")
    private String email;
    @NotEmpty(message = "{error.password.empty.or.null}")
    private String password;
    @JsonProperty("redirect_url")
    String redirectUrl;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
