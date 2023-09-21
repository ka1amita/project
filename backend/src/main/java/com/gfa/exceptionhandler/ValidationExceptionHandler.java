package com.gfa.exceptionhandler;

import com.gfa.dtos.responsedtos.ErrorResponseDTO;
import com.gfa.exceptions.email.EmailSendingFailedException;
import com.gfa.exceptions.activation.InvalidActivationCodeException;
import com.gfa.exceptions.user.InvalidResetCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(errorMsg));
    }

    @ExceptionHandler(InvalidActivationCodeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidActivationCodeException(InvalidActivationCodeException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(InvalidResetCodeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidResetCodeException(InvalidResetCodeException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(EmailSendingFailedException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailSendingException(EmailSendingFailedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(e.getMessage()));
    }

}
