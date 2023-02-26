package com.prodyna.movieapp.repository;

import com.prodyna.movieapp.domain.Movie;
import com.prodyna.movieapp.enumeration.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataNeo4jTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie avatarMovie;
    private Movie titanicMovie;

    @BeforeEach
    void init() {
        avatarMovie = new Movie();
        avatarMovie.setName("Avatar");
        avatarMovie.setDescription("Avatar is action movie.");
        avatarMovie.setReleaseDate(new Date(2022, 05, 10));
        avatarMovie.setDurationInMin(150.5D);
        avatarMovie.setCreatedAt(LocalDateTime.of(2022, 11, 23, 8, 51, 07));
        avatarMovie.setModifiedAt(LocalDateTime.of(2022, 11, 23, 8, 51, 07));
        avatarMovie.setAvgRate(5.0);
        avatarMovie.setGenre(Collections.singletonList(Genre.ACTION));

        titanicMovie = new Movie();
        titanicMovie.setName("Titanic");
        titanicMovie.setDescription("Titanic is action movie.");
        titanicMovie.setReleaseDate(new Date(2022, 05, 10));
        titanicMovie.setDurationInMin(350.5D);
        titanicMovie.setCreatedAt(LocalDateTime.of(2022, 11, 23, 8, 51, 07));
        titanicMovie.setModifiedAt(LocalDateTime.of(2022, 11, 23, 8, 51, 07));
        titanicMovie.setAvgRate(3.7);
        titanicMovie.setGenre(Collections.singletonList(Genre.DRAMA));
    }

    @Test
    @DisplayName("It should save the movie to the database")
    void save() {
        Movie newMovie = movieRepository.save(avatarMovie);
        assertNotNull(newMovie);
        assertThat(newMovie.getId()).isNotEqualTo(null);
    }

    @Test
    @DisplayName("It should return the movies list with size 2")
    void getAllMovies() {
        movieRepository.save(avatarMovie);
        movieRepository.save(titanicMovie);

        List<Movie> movieList = movieRepository.findAll();

        assertNotNull(movieList);
        assertThat(movieList).isNotNull();
        assertEquals(2, movieList.size());
    }

    @Test
    @DisplayName("It should return movie by its id")
    void getMovieById() {
        movieRepository.save(avatarMovie);

        Movie newMovie = movieRepository.findById(avatarMovie.getId()).get();
        assertNotNull(newMovie);
        assertEquals("Action", newMovie.getGenre());
        assertThat(newMovie.getReleaseDate()).isBefore(new Date(2022, 05, 10));
    }

    @Test
    @DisplayName("It should update the movie genre with ADVENTURE")
    void updateMovie() {

        movieRepository.save(avatarMovie);

        Movie existingMovie = movieRepository.findById(avatarMovie.getId()).get();
        existingMovie.setGenre(List.of(Genre.ADVENTURE));
        Movie updatedMovie = movieRepository.save(existingMovie);

        assertEquals(List.of(Genre.ADVENTURE), updatedMovie.getGenre());
        assertEquals("Avatar", updatedMovie.getName());
    }

    @Test
    @DisplayName("It should delete the existing movie")
    void deleteMovie() {

        movieRepository.save(avatarMovie);
        Long id = avatarMovie.getId();

        movieRepository.save(titanicMovie);

        movieRepository.delete(avatarMovie);

        List<Movie> movieList = movieRepository.findAll();

        Optional<Movie> exitingMovie = movieRepository.findById(id);

        assertEquals(1, movieList.size());
        assertThat(exitingMovie).isEmpty();

    }
}