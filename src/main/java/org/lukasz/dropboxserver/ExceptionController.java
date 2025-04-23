package org.lukasz.dropboxserver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(FileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Error ISE(FileException ex) {
        return new Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }
}
