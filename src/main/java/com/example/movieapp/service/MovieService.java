package com.example.movieapp.service;

import com.example.movieapp.dto.ActorDTO;
import com.example.movieapp.dto.MovieDTO;
import com.example.movieapp.dto.ReviewDTO;

import java.util.List;

public interface MovieService {

    List<MovieDTO> getAll();

    MovieDTO getById(Long id);

    MovieDTO create(MovieDTO movieDTO);

    MovieDTO update(Long id, MovieDTO movieDTO);

    void delete(Long id);

    List<ActorDTO> findAllActorsByMovieId(Long movieId);

    void addActorToMovie(Long id, ActorDTO actorDTO);

    void deleteActorFromMovie(Long id, Long actorId);

    ReviewDTO createReview(Long id, ReviewDTO reviewDTO);

    void deleteReview(Long id, Long reviewId);
    List<ReviewDTO> findAllReviewsByMovieId(Long movieId);

    List<MovieDTO> searchMoviesByNameOrGenre(String name, String genre, Integer page, Integer size);
}