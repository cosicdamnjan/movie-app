package com.example.movieapp.error.exceptions;

public class MovieAlreadyExistsException extends RuntimeException {

    public MovieAlreadyExistsException(String message) {

        super(message);
    }
}

