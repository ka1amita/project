package com.gfa.dtos.requestdtos;

import javax.validation.constraints.NotEmpty;

public class PasswordResetWithCodeRequestDTO extends RequestDTO {
    @NotEmpty(message = "Password cannot be null or empty")
    String password;

    public PasswordResetWithCodeRequestDTO() {
    }

    public PasswordResetWithCodeRequestDTO(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
