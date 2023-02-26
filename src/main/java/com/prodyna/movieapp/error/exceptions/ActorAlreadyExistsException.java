package com.prodyna.movieapp.error.exceptions;

public class ActorAlreadyExistsException extends RuntimeException {

    public ActorAlreadyExistsException(String message) {
        super(message);
    }
}
