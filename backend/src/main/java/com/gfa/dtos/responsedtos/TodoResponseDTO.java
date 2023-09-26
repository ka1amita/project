package com.gfa.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gfa.models.AppUser;
import com.gfa.models.Todo;

import java.time.LocalDateTime;

public class TodoResponseDTO extends ResponseDTO {

    public final Long id;
    public final String title;
    public final String description;
    @JsonProperty("due_date")
    public final String dueDate;
    @JsonProperty("app_user")
    public final String appUser;

    public TodoResponseDTO(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.dueDate = todo.getDueDate().toString();
        this.appUser = todo.getAppUser().getUsername();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }
}
