package com.gfa.dtos.requestdtos;

public class UpdateAppUserDTO {

    public final String username;
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
