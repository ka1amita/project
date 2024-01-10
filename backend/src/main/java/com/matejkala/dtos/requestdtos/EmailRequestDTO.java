package com.matejkala.dtos.requestdtos;

public class EmailRequestDTO extends RequestDTO {

    public final String email;

    public EmailRequestDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
