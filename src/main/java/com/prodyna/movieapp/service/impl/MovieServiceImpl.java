package com.prodyna.movieapp.service.impl;

import com.prodyna.movieapp.domain.Actor;
import com.prodyna.movieapp.domain.Movie;
import com.prodyna.movieapp.dto.ActorDTO;
import com.prodyna.movieapp.dto.MovieDTO;
import com.prodyna.movieapp.dto.ReviewDTO;
import com.prodyna.movieapp.error.exceptions.MovieAlreadyExistsException;
import com.prodyna.movieapp.error.exceptions.MovieNotFoundException;
import com.prodyna.movieapp.mapper.MovieMapper;
import com.prodyna.movieapp.repository.ActorRepository;
import com.prodyna.movieapp.repository.MovieRepository;
import com.prodyna.movieapp.service.ActorService;
import com.prodyna.movieapp.service.MovieService;
import com.prodyna.movieapp.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

    private static final String NOT_FOUND = "Movie not found";
    private static final String ALREADY_EXISTS = "Movie already exists";
    private final MovieRepository movieRepository;

    private final ActorRepository actorRepository;
    private final ActorService actorService;

    private final ReviewService reviewService;
    private final MovieMapper mapper;

    @Override
    public List<MovieDTO> getAll() {
        List<MovieDTO> movieDTOList = mapper.mapToMovieDTOList(movieRepository.findAll());

        return movieDTOList.stream()
                .sorted(Comparator.comparingDouble(MovieDTO::getAvgRate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public MovieDTO getById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(NOT_FOUND));
        return mapper.mapToMovieDTO(movie);
    }

    @Override
    public MovieDTO create(MovieDTO movieDTO) {
        Optional<Movie> movieExists = movieRepository.findByNameAndReleaseDate(movieDTO.getName(), movieDTO.getReleaseDate());
        if (movieExists.isPresent()) {
            throw new MovieAlreadyExistsException(ALREADY_EXISTS);
        }

        Movie movie = mapper.mapToMovie(movieDTO);
        checkIfActorExists(movie);
        movieRepository.save(movie);

        return mapper.mapToMovieDTO(movie);
    }

    private void checkIfActorExists(Movie movie) {
        if (movie == null || movie.getActors() == null) {
            return;
        }

        Set<Actor> actors = new HashSet<>(movie.getActors());
        actors.forEach(actor -> {
            if (actor == null) {
                return;
            }

            Optional<Actor> existingActor = actorRepository.findByFirstNameAndLastName(actor.getFirstName(), actor.getLastName());
            if (existingActor.isPresent()) {
                movie.getActors().remove(actor);
                movie.getActors().add(existingActor.get());
            }
        });
    }

    @Override
    public MovieDTO update(Long id, MovieDTO movieDTO) {
        Movie movieDB = movieRepository.findById(id).orElseThrow(
                () -> new MovieNotFoundException(NOT_FOUND));

        Optional<Movie> optionalMovie = movieRepository.findByNameAndReleaseDate(movieDTO.getName(), movieDTO.getReleaseDate());

        if (optionalMovie.isPresent() && !Objects.equals(optionalMovie.get().getId(), movieDB.getId())) {
            throw new MovieAlreadyExistsException(ALREADY_EXISTS);
        }

        movieDB.setName(movieDB.getName());
        movieDB.setDescription(movieDB.getDescription());
        movieDB.setReleaseDate(movieDB.getReleaseDate());
        movieDB.setDurationInMin(movieDB.getDurationInMin());

        movieRepository.save(movieDB);
        return mapper.mapToMovieDTO(movieDB);
    }

    @Override
    public void delete(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(NOT_FOUND));
        movieRepository.deleteById(movie.getId());
    }

    @Override
    public List<ActorDTO> findAllActorsByMovieId(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(NOT_FOUND);
        }

        List<ActorDTO> actors = actorService.findActorsByMovieId(movieId);

        return actors;
    }

    @Override
    public void addActorToMovie(Long id, ActorDTO actorDTO) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new MovieNotFoundException(NOT_FOUND);
        }

        actorService.addActorToMovie(id, actorDTO);
    }

    @Override
    public void deleteActorFromMovie(Long id, Long actorId) {
        Optional<Movie> movie = movieRepository.findById(id);
        if (movie.isEmpty()) {
            throw new MovieNotFoundException(NOT_FOUND);
        }

        actorService.deleteActorFromMovie(id, actorId);
    }

    @Override
    public ReviewDTO createReview(Long id, ReviewDTO reviewDTO) {
        Optional<Movie> movie = movieRepository.findById(id);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(NOT_FOUND);
        }

        ReviewDTO review = reviewService.createReview(id, reviewDTO);

        return review;
    }

    @Override
    public void deleteReview(Long id, Long reviewId) {
        Optional<Movie> movie = movieRepository.findById(id);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(NOT_FOUND);
        }

        reviewService.deleteReview(id, reviewId);
    }

    public List<ReviewDTO> findAllReviewsByMovieId(Long movieId) {
        Optional<Movie> movie = movieRepository.findById(movieId);

        if (movie.isEmpty()) {
            throw new MovieNotFoundException(NOT_FOUND);
        }

        List<ReviewDTO> reviews = reviewService.findReviewsByMovieId(movieId);

        return reviews;
    }

    public List<MovieDTO> searchMoviesByNameOrGenre(String name, String genre, Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Movie> movies = movieRepository.searchMoviesByNameOrGenre(name, genre, paging);

        return movies.stream()
                .map(mapper::mapToMovieDTO)
                .sorted(Comparator.comparing(MovieDTO::getAvgRate).reversed())
                .collect(Collectors.toList());
    }
}
