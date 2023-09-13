package com.gfa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "activation_codes")
@JsonIgnoreProperties("appUser")
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    private String activationCode;
    @ManyToOne
    private AppUser appUser;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ActivationCode() {
    }

    public ActivationCode(String activationCode, AppUser appUser) {
        this.activationCode = activationCode;
        this.appUser = appUser;
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
