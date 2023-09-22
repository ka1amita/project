package com.gfa.exceptionhandler;

import com.gfa.dtos.responsedtos.ErrorResponseDTO;
import com.gfa.exceptions.activation.ActivationCodeExpiredException;
import com.gfa.exceptions.email.EmailAlreadyExistsException;
import com.gfa.exceptions.role.RoleNotFoundException;
import com.gfa.exceptions.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordFormatException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPasswordFormatException(InvalidPasswordFormatException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(ActivationCodeExpiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleActivationCodeExpiredException(ActivationCodeExpiredException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoleNotFoundException(RoleNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(MissingJSONBodyException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingJSONBodyException(MissingJSONBodyException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(InvalidPatchDataException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidPatchDataException(InvalidPatchDataException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(405).body(new ErrorResponseDTO(e.getMessage()));
    }
}
