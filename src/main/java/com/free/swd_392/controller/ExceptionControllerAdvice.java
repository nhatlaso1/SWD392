package com.free.swd_392.controller;

import com.free.swd_392.dto.error.ErrorResponse;
import com.free.swd_392.exception.InvalidException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerErrorException;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ExceptionHandler({InvalidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidException(InvalidException e) {
        log.error("Business Error: {}", e.getMessage());
        return ErrorResponse.builder()
                .message(e.getMessage())
                .success(false)
                .build();
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.error("", e);
        return ErrorResponse.builder()
                .message(e.getMessage())
                .success(false)
                .build();
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentInvalidException(BindException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, Objects.requireNonNullElse(errorMessage, ""));
        });

        return ErrorResponse.builder()
                .data(errors)
                .success(false)
                .data(errors)
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return ErrorResponse.builder()
                .success(false)
                .message("Access denied")
                .build();
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getMessage();
            errors.put(fieldName, Objects.requireNonNullElse(errorMessage, ""));
        });

        return ErrorResponse.builder()
                .success(false)
                .data(errors)
                .build();
    }

    @ExceptionHandler({PropertyReferenceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePropertyReferenceException(PropertyReferenceException e) {
        return ErrorResponse.builder()
                .success(false)
                .data(Map.of(e.getPropertyName(), String.format("Field %s is not accepted", e.getPropertyName())))
                .build();
    }

    @ExceptionHandler({UndeclaredThrowableException.class})
    public ErrorResponse handleUndeclaredThrowableException(UndeclaredThrowableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidException invalidException) {
            return this.handleInvalidException(invalidException);
        }
        return this.exceptionHandler(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            ServerErrorException.class,
            HttpServerErrorException.class,
            NullPointerException.class,
            HttpMessageNotWritableException.class,
            ConversionNotSupportedException.class,
            MissingPathVariableException.class,
            RuntimeException.class
    })
    public ErrorResponse exceptionHandler(Exception e) {
        log.error(e.getClass().getSimpleName() + ": " + e.getMessage() + " cause: ", e);
        return ErrorResponse.builder()
                .success(false)
                .data("SERVER ERROR")
                .build();
    }
}
