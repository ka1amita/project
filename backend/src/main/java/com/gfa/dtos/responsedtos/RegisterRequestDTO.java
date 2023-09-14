package com.gfa.dtos.responsedtos;

import javax.validation.constraints.NotEmpty;


public class RegisterRequestDTO extends RequestDTO {
    @NotEmpty(message = "Username cannot be null or empty")
    private String username;
    @NotEmpty(message = "Email cannot be null or empty")
    private String email;
    @NotEmpty(message = "Password cannot be null or empty")
    private String password;

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
}
