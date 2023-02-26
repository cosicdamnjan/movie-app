package com.prodyna.movieapp.service;

import com.prodyna.movieapp.domain.Actor;
import com.prodyna.movieapp.domain.Movie;
import java.util.List;

public interface ImportJsonData {

    void convertData();

    void processMovie(List<Movie> movies);

    void processActor(Movie movie);

    Actor actorCreation(Actor actor);
}
