package com.gfa.dtos.responsedtos;

public class helloMessageDTO {
    String message;

    public helloMessageDTO() {
    }

    public helloMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
