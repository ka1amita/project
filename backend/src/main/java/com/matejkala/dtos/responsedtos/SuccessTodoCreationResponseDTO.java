package com.matejkala.dtos.responsedtos;

public class SuccessTodoCreationResponseDTO extends ResponseDTO {

    public final String message;

    public SuccessTodoCreationResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
