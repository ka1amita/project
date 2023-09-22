package com.gfa.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.gfa.models.AppUser;

import java.time.LocalDateTime;

public class AppUserResponseDTO extends ResponseDTO {

    public final Long id;
    public final String username;
    public final String email;
    public final LocalDateTime verified_at;

    public AppUserResponseDTO(AppUser appUser) {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.email = appUser.getEmail();
        verified_at = appUser.getVerified_at();
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

    public LocalDateTime getVerified_at() {
        return verified_at;
    }
}
