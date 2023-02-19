package com.example.movieapp.controller;

import com.example.movieapp.dto.ActorDTO;
import com.example.movieapp.dto.MovieDTO;
import com.example.movieapp.dto.ReviewDTO;
import com.example.movieapp.service.impl.MovieServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieServiceImpl movieService;

    @GetMapping
    public ResponseEntity<List<MovieDTO>> findAll() {

        List<MovieDTO> movies = movieService.getAll();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getById(@PathVariable Long id) {

        MovieDTO movieDTO = movieService.getById(id);
        return new ResponseEntity<>(movieDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> create(@RequestBody @Valid MovieDTO movieDTO) {

        MovieDTO movie = movieService.create(movieDTO);

        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> update(@PathVariable Long id, @RequestBody MovieDTO movieDTO) {

        MovieDTO movie = movieService.update(id, movieDTO);

        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {

        movieService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/actors")
    public ResponseEntity<List<ActorDTO>> findAllActorsByMovieId(@PathVariable Long id) {

        List<ActorDTO> actors = movieService.findAllActorsByMovieId(id);

        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @PatchMapping("/{id}/actors")
    public ResponseEntity<HttpStatus> addActorToMovie(@PathVariable Long id, @RequestBody @Valid ActorDTO actorDTO) {

        movieService.addActorToMovie(id, actorDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/actors/{actorId}")
    public ResponseEntity<HttpStatus> deleteActorFromMovie(@PathVariable Long id, @PathVariable Long actorId) {

        movieService.deleteActorFromMovie(id, actorId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDTO> saveReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {

        ReviewDTO review = movieService.createReview(id, reviewDTO);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/reviews/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long id, @PathVariable Long reviewId) {

        movieService.deleteReview(id, reviewId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> findAllReviewsByMovieId(@PathVariable Long id) {

        List<ReviewDTO> reviews = movieService.findAllReviewsByMovieId(id);

        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(
            @RequestParam(value = "name", defaultValue = "", required = false) String name,
            @RequestParam(value = "genre", defaultValue = "", required = false) String genre,
            @RequestParam(value = "page", defaultValue = "0", required = false) @PositiveOrZero Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size) {

        List<MovieDTO> movieDTOs = movieService.searchMoviesByNameOrGenre(name, genre, page, size);

        return new ResponseEntity<>(movieDTOs, HttpStatus.OK);
    }
}