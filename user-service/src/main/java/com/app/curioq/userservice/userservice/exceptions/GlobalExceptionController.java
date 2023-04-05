package com.app.curioq.userservice.userservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage genericException(GenericException ex, WebRequest request) {

        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage());
    }

    @ExceptionHandler(InvalidLoginException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ErrorMessage invalidLoginException(InvalidLoginException ex, WebRequest request) {

        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyPresentException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage userPresentException(UserAlreadyPresentException ex, WebRequest request) {

        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage validationException(ValidationException ex, WebRequest request) {

        return new ErrorMessage(
                LocalDateTime.now(),
                ex.getMessage());
    }
}
