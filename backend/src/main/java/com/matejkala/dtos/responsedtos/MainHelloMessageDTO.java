package com.matejkala.dtos.responsedtos;

public class MainHelloMessageDTO {
    String message;

    public MainHelloMessageDTO() {
    }

    public MainHelloMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
