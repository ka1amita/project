package com.gfa.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponseDTO extends ResponseDTO {

    @JsonProperty("error_message")
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
