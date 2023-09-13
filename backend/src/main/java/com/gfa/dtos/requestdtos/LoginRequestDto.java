package com.gfa.dtos.requestdtos;

public class LoginRequestDto extends RequestDto {

    private String loginInput;
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String loginInput, String password) {
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
