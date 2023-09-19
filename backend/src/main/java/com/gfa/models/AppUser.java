package com.gfa.models;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private boolean active;
    @ManyToMany(fetch = EAGER, cascade = ALL)
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
    }

    public AppUser(String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        active = false;
    }

    public AppUser(Long id, String username, String password, String email, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        active = false;
    }

    public AppUser(Long id, String username, String password, String email, boolean active,
                   Set<Role> roles, Set<ActivationCode> activationCodes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.active = active;
        this.roles = roles;
        this.activationCodes = activationCodes;
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

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @Override
    public Set<Role> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(@NotNull boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(@NotNull Set<Role> roles) {
        this.roles = roles;
    }

    public Set<ActivationCode> getActivationCodes() {
        return activationCodes;
    }

    public void setActivationCodes(@NotNull Set<ActivationCode> activationCodes) {
        this.activationCodes = activationCodes;
    }


}
