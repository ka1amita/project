package com.gfa.models;

import static javax.persistence.FetchType.EAGER;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private boolean active;
    @ManyToMany(fetch = EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
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

