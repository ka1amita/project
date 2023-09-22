package com.gfa.models;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.EAGER;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "app_users")
@Where(clause = "deleted=false")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;
    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime verified_at = null;
    private boolean active;
    private boolean deleted;
    @ManyToMany(fetch = EAGER, cascade = {MERGE})
    @JoinTable(
            name = "app_users_roles",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @JsonManagedReference
    private Set<ActivationCode> activationCodes = new HashSet<>();

    public AppUser() {
    }

    public AppUser(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        created_at = LocalDateTime.now();
    }

    public AppUser(String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        created_at = LocalDateTime.now();
    }

    public AppUser(Long id, String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        created_at = LocalDateTime.now();
    }

    public AppUser(Long id, String username, String password, String email,
                   Set<Role> roles, Set<ActivationCode> activationCodes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.activationCodes = activationCodes;
        created_at = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Set<Role> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<ActivationCode> getActivationCodes() {
        return activationCodes;
    }

    public void setActivationCodes(Set<ActivationCode> activationCodes) {
        this.activationCodes = activationCodes;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getVerified_at() {
        return verified_at;
    }

    public void setVerified_at(LocalDateTime verified_at) {
        this.verified_at = verified_at;
    }

    public boolean hasValidRoles() {
        for (Role role : roles) {
            if (!role.isValidRole()) {
                return false;
            }
        }
        return true;
    }

    public void assignRole(Role role) {
        if (role.isValidRole()) {
            this.roles.add(role);
        } else {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    public void removeRole(Role role) {
        if (role.isValidRole()) {
            this.roles.remove(role);
        } else {
            throw new IllegalArgumentException("Invalid role");
        }
    }
}

