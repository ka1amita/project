package com.gfa.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class PasswordResetRequestDTO extends RequestDTO {
    @NotEmpty(message = "{error.not.empty.username.or.email}")
    @JsonProperty("email")
    @JsonAlias("username")
    String usernameOrEmail;

    public PasswordResetRequestDTO() {
    }

    public PasswordResetRequestDTO(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
