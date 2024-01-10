package com.matejkala.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matejkala.models.Todo;

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
