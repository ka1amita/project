package com.gfa.dtos.responsedtos;

import com.gfa.models.AppUser;
import com.gfa.models.Todo;

import java.time.LocalDateTime;

public class TodoResponseDTO extends ResponseDTO {

    public final String title;
    public final String description;
    public final LocalDateTime dueDate;
    public final AppUser appUser;

    public TodoResponseDTO(Todo todo) {
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.dueDate = todo.getDueDate();
        this.appUser = todo.getAppUser();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
