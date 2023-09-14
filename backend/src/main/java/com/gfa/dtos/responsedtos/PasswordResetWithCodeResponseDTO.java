package com.gfa.dtos.responsedtos;

public class PasswordResetWithCodeResponseDTO extends ResponseDTO {
    String message;

    public PasswordResetWithCodeResponseDTO() {
    }

    public PasswordResetWithCodeResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
