package com.gfa.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class PasswordResetRequestDTO extends RequestDTO {
    @NotEmpty(message = "{error.not.empty.username.or.email}")
    @JsonProperty("email")
    @JsonAlias("username")
    String usernameOrEmail;

    String redirect_url;

    public PasswordResetRequestDTO() {
    }

    public PasswordResetRequestDTO(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public PasswordResetRequestDTO(String usernameOrEmail, String redirect_url) {
        this.usernameOrEmail = usernameOrEmail;
        this.redirect_url = redirect_url;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }
}
