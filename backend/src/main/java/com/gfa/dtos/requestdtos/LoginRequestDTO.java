package com.gfa.dtos.requestdtos;

public class LoginRequestDTO extends RequestDTO {

    private String loginInput;
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String loginInput, String password) {
        this.loginInput = loginInput;
        this.password = password;
    }

    public String getLoginInput() {
        return loginInput;
    }

    public void setLoginInput(String loginInput) {
        this.loginInput = loginInput;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
