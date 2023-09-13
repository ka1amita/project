package com.gfa.dtos.responsedtos;

public class LoginResponseDTO extends ResponseDTO {

    private String jwtToken;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
