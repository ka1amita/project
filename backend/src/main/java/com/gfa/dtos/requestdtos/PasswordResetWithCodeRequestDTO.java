package com.gfa.dtos.requestdtos;

public class PasswordResetWithCodeRequestDTO extends RequestDTO {
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
