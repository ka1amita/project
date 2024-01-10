package com.matejkala.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class PasswordResetRequestDTO extends RequestDTO {
    @NotEmpty(message = "{error.not.empty.username.or.email}")
    @JsonProperty("email")
    @JsonAlias("username")
    String usernameOrEmail;
    @JsonProperty("redirect_url")
    String redirectUrl;

    public PasswordResetRequestDTO() {
    }

    public PasswordResetRequestDTO(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public PasswordResetRequestDTO(String usernameOrEmail, String redirectUrl) {
        this.usernameOrEmail = usernameOrEmail;
        this.redirectUrl = redirectUrl;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
