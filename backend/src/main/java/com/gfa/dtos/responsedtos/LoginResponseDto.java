package com.gfa.dtos.responsedtos;

public class LoginResponseDto extends ResponseDto {

    private String jwtToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
