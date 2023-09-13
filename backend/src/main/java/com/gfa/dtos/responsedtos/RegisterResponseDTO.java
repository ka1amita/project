package com.gfa.dtos.responsedtos;

public class RegisterResponseDTO extends ResponseDTO{
    private String message;

    public RegisterResponseDTO() {
    }

    public RegisterResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
