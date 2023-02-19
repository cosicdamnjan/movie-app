package com.example.movieapp.service;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Movie;

import java.util.List;

public interface ImportJsonData {

    void convertData();

    void processMovie(List<Movie> movies);

    void processActor(Movie movie);

    Actor actorCreation(Actor actor);
}