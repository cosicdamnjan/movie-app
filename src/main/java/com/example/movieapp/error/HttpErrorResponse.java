package com.example.movieapp.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class HttpErrorResponse {

    private final String path;
    private final String message;
    private final HttpStatus error;
    private final Integer status;
    private final LocalDateTime timestamp;
}
