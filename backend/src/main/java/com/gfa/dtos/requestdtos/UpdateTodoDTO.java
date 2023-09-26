package com.gfa.dtos.requestdtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateTodoDTO extends RequestDTO {

    public final String title;
    public final String description;
    @JsonProperty("due_date")
    public final String dueDate;
    @JsonProperty("app_user")
    public final String appUser;

    public UpdateTodoDTO(String title, String description, String dueDate, String appUser) {
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
