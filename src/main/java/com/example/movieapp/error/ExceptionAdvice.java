package com.example.movieapp.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler({MovieNotFoundException.class, ActorNotFoundException.class, ReviewNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<HttpErrorResponse> handleNotFound(RuntimeException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        return createHttpResponse(status, exception.getMessage(), request.getRequestURI());
    }

    @ResponseBody
    @ExceptionHandler({MovieAlreadyExistsException.class, ActorAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<HttpErrorResponse> handleBadRequest(RuntimeException exception, HttpServletRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return createHttpResponse(status, exception.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<HttpErrorResponse> createHttpResponse(HttpStatus httpStatus, String message, String path) {

        return new ResponseEntity<>(new HttpErrorResponse(
                path, message, httpStatus, httpStatus.value(), LocalDateTime.now()), httpStatus);
    }
}