package com.example.movieapp.error.exceptions;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(String message) {

        super(message);
    }
}

