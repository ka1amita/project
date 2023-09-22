package com.gfa.dtos.requestdtos;

public class RequestTokenDTO extends RequestDTO{
    private String token;
    private String issuer;

    public RequestTokenDTO(String token, String issuer) {
        this.token = token;
        this.issuer = issuer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
