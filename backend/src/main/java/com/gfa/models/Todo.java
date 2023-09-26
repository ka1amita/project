package com.gfa.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    @ManyToOne
    private AppUser appUser;

    public Todo() {
    }

    public Todo(Long id, String title, String description, LocalDateTime dueDate, AppUser appUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.appUser = appUser;
    }

    public Todo(String title, String description, LocalDateTime dueDate, AppUser appUser) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.appUser = appUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
