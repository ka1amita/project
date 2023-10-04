package com.gfa.exceptionhandler;

import com.gfa.dtos.responsedtos.ErrorResponseDTO;
import com.gfa.exceptions.activation.InvalidActivationCodeException;
import com.gfa.exceptions.todo.TodoAlreadyExistsWithThisNameForThisUserException;
import com.gfa.exceptions.todo.TodoNotFoundException;
import com.gfa.exceptions.email.EmailFormatException;
import com.gfa.exceptions.user.InvalidIdException;
import com.gfa.exceptions.user.InvalidResetCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {

    private final MessageSource messageSource;
    @Autowired
    public ValidationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponseDTO> handleMessagingException(MessagingException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidIdException(InvalidIdException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(TodoAlreadyExistsWithThisNameForThisUserException.class)
    public ResponseEntity<ErrorResponseDTO> handleTodoAlreadyExistsWithThisNameForThisUserException(TodoAlreadyExistsWithThisNameForThisUserException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleTodoNotFoundException(TodoNotFoundException e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDTO(e.getMessage()));
    }
}
