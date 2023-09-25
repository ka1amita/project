package com.gfa.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gfa.models.AppUser;

import java.time.LocalDateTime;

public class AppUserResponseDTO extends ResponseDTO {

    public final Long id;
    public final String username;
    public final String email;
    @JsonProperty("verified_at")
    public final LocalDateTime verifiedAt;

    public AppUserResponseDTO(AppUser appUser) {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.email = appUser.getEmail();
        verifiedAt = appUser.getVerifiedAt();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }
}
