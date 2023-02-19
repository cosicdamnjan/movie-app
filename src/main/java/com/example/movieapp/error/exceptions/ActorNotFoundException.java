package com.example.movieapp.error.exceptions;

public class ActorNotFoundException extends RuntimeException {

    public ActorNotFoundException(String message) {

        super(message);
    }
}
