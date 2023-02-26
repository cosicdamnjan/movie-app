package com.prodyna.movieapp.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prodyna.movieapp.domain.Actor;
import com.prodyna.movieapp.domain.Movie;
import com.prodyna.movieapp.dto.JsonDataDTO;
import com.prodyna.movieapp.repository.ActorRepository;
import com.prodyna.movieapp.repository.MovieRepository;
import com.prodyna.movieapp.service.ImportJsonData;
import com.prodyna.movieapp.service.ValidationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class ImportJsonDataImpl implements ImportJsonData {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;
    private final ValidationService validationService;

    @Override
    public void convertData() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<JsonDataDTO> typeReference = new TypeReference<>() {};

        InputStream inputStream = TypeReference.class.getResourceAsStream("/data.json");

        try {
            JsonDataDTO jsonDataDTO = mapper.readValue(inputStream, typeReference);
            processMovie(jsonDataDTO.getMovies());
            log.info("Initial Data loaded!");

        } catch (IOException e) {
            log.info("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void processMovie(List<Movie> movies) {

        List<Movie> movieList = new ArrayList<>();

        movies.stream()
                .filter(x -> movieRepository.findByNameAndReleaseDate(x.getName(), x.getReleaseDate()).isEmpty())
                .forEach(x -> {
                    validationService.validate(x);
                    processActor(x);
                    movieList.add(x);
                });

        movieRepository.saveAll(movieList);
    }

    @Override
    public void processActor(Movie movie) {

        List<Actor> actorList = new ArrayList<>();

        for (Actor actor : movie.getActors()) {
            Optional<Actor> optionalActor = actorRepository
                    .findByFirstNameAndLastName(actor.getFirstName(), actor.getLastName());

            Actor savedActor = optionalActor.isPresent() ? optionalActor.get() : actorCreation(actor);
            actorList.add(savedActor);
        }
        movie.setActors(actorList);
    }

    @Override
    public Actor actorCreation(Actor actor) {
        validationService.validate(actor);
        return actorRepository.save(actor);
    }
}
