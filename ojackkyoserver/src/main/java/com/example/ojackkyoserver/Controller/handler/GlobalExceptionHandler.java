package com.example.ojackkyoserver.Controller.handler;

import com.example.ojackkyoserver.exceptions.Error400;
import com.example.ojackkyoserver.exceptions.Error401;
import com.example.ojackkyoserver.exceptions.Error403;
import com.example.ojackkyoserver.exceptions.Error404;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Error400.class)
    public String handleBadRequestException(Error400 e) {
        return e.getMessage();
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = Error401.class)
    public String handleUnauthorizedException(Error401 e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = Error403.class)
    public String handleForbiddenError(Error403 e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = Error404.class)
    public String handleNotFoundException(Error404 e) {
        return e.getMessage();
    }

}