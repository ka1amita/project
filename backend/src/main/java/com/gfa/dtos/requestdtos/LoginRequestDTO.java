package com.gfa.dtos.requestdtos;

public class LoginRequestDTO {

    public final String loginInput;
    public final String password;

    public LoginRequestDTO(String loginInput, String password) {
        this.loginInput = loginInput;
        this.password = password;
    }

    public String getLoginInput() {
        return loginInput;
    }

    public String getPassword() {
        return password;
    }
}
