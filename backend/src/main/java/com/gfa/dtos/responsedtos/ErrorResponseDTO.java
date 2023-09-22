package com.gfa.dtos.responsedtos;

public class ErrorResponseDTO extends ResponseDTO {

    private String errorMessage;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}