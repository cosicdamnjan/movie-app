package com.example.movieapp.error.exceptions;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(String message) {

        super(message);
    }
}

