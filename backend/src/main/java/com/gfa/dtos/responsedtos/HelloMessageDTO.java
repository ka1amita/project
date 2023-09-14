package com.gfa.dtos.responsedtos;

public class HelloMessageDTO {
    String message;

    public HelloMessageDTO() {
    }

    public HelloMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
