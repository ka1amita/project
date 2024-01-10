package com.matejkala.dtos.requestdtos;

public class IdRequestDTO extends RequestDTO {

    public final Long id;

    public IdRequestDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
