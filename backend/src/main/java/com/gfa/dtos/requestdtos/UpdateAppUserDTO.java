package com.gfa.dtos.requestdtos;

import javax.validation.constraints.Email;

public class UpdateAppUserDTO {

    public final String username;
    @Email(message = "{error.email.invalid.format}")
    public final String email;
    public final String password;

    public UpdateAppUserDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
