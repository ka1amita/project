package com.gfa.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gfa.models.AppUser;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TodoCreateRequestDTO extends RequestDTO {

    @NotEmpty(message = "Title can't be null or empty")
    public final String title;
    @NotEmpty(message = "Description can't be null or empty")
    public final String description;
    @JsonProperty("due_date")
    @NotEmpty(message = "Due date can't be null or empty")
    public final String dueDate;
    @JsonProperty("app_user")
    @NotEmpty(message = "App user can't be null or empty")
    public final String appUser;

    public TodoCreateRequestDTO(String title, String description, String dueDate, String appUser) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.appUser = appUser;
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

    public String getAppUser() {
        return appUser;
    }
}
