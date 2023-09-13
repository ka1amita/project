package com.gfa.controllers;

import com.gfa.dtos.responsedtos.ErrorResponseDTO;
import com.gfa.exceptions.EmailAlreadyExistsException;
import com.gfa.exceptions.InvalidActivationCodeException;
import com.gfa.exceptions.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(InvalidActivationCodeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidActivationCodeException(InvalidActivationCodeException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException e){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

}
