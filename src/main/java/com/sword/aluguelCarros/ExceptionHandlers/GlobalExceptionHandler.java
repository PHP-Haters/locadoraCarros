package com.sword.aluguelCarros.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GenericExceptions.NotFound.class)
    public ResponseEntity<String> handleNotFound(GenericExceptions.NotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(GenericExceptions.AlreadyExists.class)
    public ResponseEntity<String> handleAlreadyExists(GenericExceptions.AlreadyExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(GenericExceptions.InvalidData.class)
    public ResponseEntity<String> handleInvalidData(GenericExceptions.InvalidData ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(GenericExceptions.Unauthorized.class)
    public ResponseEntity<String> handleUnauthorized(GenericExceptions.Unauthorized ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(GenericExceptions.General.class)
    public ResponseEntity<String> handleGeneral(GenericExceptions.General ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}