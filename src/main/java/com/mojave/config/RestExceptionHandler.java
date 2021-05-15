package com.mojave.config;

import com.mojave.exception.InvalidJwtAuthenticationException;
import com.mojave.exception.InvalidTokenException;
import com.mojave.exception.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@Log4j2
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity userNotFound(UserNotFoundException ex, WebRequest request) {
        log.debug("handling UserNotFoundException...");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {InvalidJwtAuthenticationException.class})
    public ResponseEntity invalidJwtAuthentication(InvalidJwtAuthenticationException ex, WebRequest request) {
        log.debug("handling InvalidJwtAuthenticationException...");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity InvalidTokenException(InvalidTokenException ex, WebRequest request) {
        log.debug("handling InvalidTokenException...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<String> validationException(MethodArgumentNotValidException ex, WebRequest request) {
        log.debug("handling MethodArgumentNotValidException...");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<String> validationException(ConstraintViolationException ex, WebRequest request) {
        log.debug("handling ConstraintViolationException...");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
