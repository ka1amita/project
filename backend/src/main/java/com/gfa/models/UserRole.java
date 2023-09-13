package com.gfa.models;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @ManyToMany(mappedBy = "userRoles", fetch = FetchType.LAZY)
    private List<AppUser> appUsers = new ArrayList<>();

    public UserRole() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AppUser> getUsers() {
        return appUsers;
    }

    public void setUsers(List<AppUser> appUsers) {
        this.appUsers = appUsers;
    }
}
