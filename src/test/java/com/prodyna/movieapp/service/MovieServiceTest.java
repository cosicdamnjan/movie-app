package com.prodyna.movieapp.service;

import com.prodyna.movieapp.domain.Movie;
import com.prodyna.movieapp.dto.MovieDTO;
import com.prodyna.movieapp.enumeration.Genre;
import com.prodyna.movieapp.error.exceptions.MovieAlreadyExistsException;
import com.prodyna.movieapp.error.exceptions.MovieNotFoundException;
import com.prodyna.movieapp.mapper.MovieMapper;
import com.prodyna.movieapp.repository.MovieRepository;
import com.prodyna.movieapp.service.impl.ActorServiceImpl;
import com.prodyna.movieapp.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ActorServiceImpl actorService;
    @Mock
    private MovieMapper movieMapper;
    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie createdMovie;
    private MovieDTO createdMovieDTO;
    private Movie mappedMovieForCreating;
    private Movie testingDate;
    private Movie testingDateUpdate;

    private Movie buildMovie(Long id, String name, List<Genre> genre, Date releaseDate, Double durationInMin, String description) {
        return Movie.builder().id(id).name(name).genre(genre).releaseDate(releaseDate).durationInMin(durationInMin).description(description).build();
    }

    private MovieDTO buildMovieDTO(String name, List<Genre> genre, Date releaseDate, Double durationInMin, String description) {
        return MovieDTO.builder().name(name).genre(genre).releaseDate(releaseDate).durationInMin(durationInMin).description(description).build();
    }

    @BeforeEach
    private void init() {

        createdMovieDTO = buildMovieDTO("The Lord of the Rings", List.of(Genre.ACTION), new Date(2001), 178.0, "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.");

        createdMovie = buildMovie(1L, "The Lord of the Rings", List.of(Genre.ACTION), new Date(2001), 178.0, "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.");

        mappedMovieForCreating = Movie.builder().name("The Lord of the Rings").genre(List.of(Genre.ACTION)).releaseDate(new Date(2001)).durationInMin(178.0).description("A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.").build();

        testingDate = Movie.builder().name("The Lord of the Rings").genre(List.of(Genre.ACTION)).releaseDate(new Date(2001)).durationInMin(178.0).description("A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.").createdAt(LocalDateTime.of(2022, 11, 23, 8, 51, 07)).modifiedAt(LocalDateTime.of(2022, 11, 23, 8, 51, 07)).build();

        testingDateUpdate = Movie.builder().name("The Lord of the Rings").genre(List.of(Genre.ACTION)).releaseDate(new Date(2001)).durationInMin(178.0).description("A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.").createdAt(LocalDateTime.now()).modifiedAt(LocalDateTime.now()).build();
    }

    @Test
    void should_createMovie() {
        when(movieMapper.mapToMovie(createdMovieDTO)).thenReturn(mappedMovieForCreating);
        when(movieRepository.findByNameAndReleaseDate(mappedMovieForCreating.getName(), mappedMovieForCreating.getReleaseDate())).thenReturn(Optional.empty());
        when(movieRepository.save(mappedMovieForCreating)).thenReturn(mappedMovieForCreating);
        when(movieMapper.mapToMovieDTO(mappedMovieForCreating)).thenReturn(createdMovieDTO);

        MovieDTO savedMovie = movieService.create(createdMovieDTO);

        Assertions.assertNotNull(savedMovie);
        Assertions.assertEquals(createdMovieDTO.getName(), savedMovie.getName());
        Assertions.assertEquals(createdMovieDTO.getReleaseDate(), savedMovie.getReleaseDate());
    }


    @Test
    void should_findById() {

        when(movieRepository.findById(1L)).thenReturn(Optional.of(createdMovie));
        when(movieMapper.mapToMovieDTO(createdMovie)).thenReturn(createdMovieDTO);

        MovieDTO movie = movieService.getById(1L);

        Assertions.assertNotNull(movie);
        Assertions.assertEquals(createdMovieDTO, movie);
    }

    @Test
    void should_updateMovie() {
        Long id = 1L;
        MovieDTO updatedMovieDTO = MovieDTO.builder()
                .id(id)
                .name("Updated Movie")
                .releaseDate(new Date(2022, Calendar.APRIL, 1))
                .durationInMin(120.0)
                .description("An updated movie")
                .build();
        Movie updatedMovie = buildMovie(id, "Updated Movie", List.of(Genre.DRAMA), new Date(2022, Calendar.APRIL, 1), 120.0, "An updated movie");

        when(movieRepository.findById(id)).thenReturn(Optional.of(createdMovie));
        when(movieRepository.findByNameAndReleaseDate(updatedMovieDTO.getName(), updatedMovieDTO.getReleaseDate())).thenReturn(Optional.empty());
        when(movieRepository.save(ArgumentMatchers.any(Movie.class))).thenReturn(updatedMovie);
        when(movieMapper.mapToMovieDTO(ArgumentMatchers.any(Movie.class))).thenReturn(updatedMovieDTO);

        MovieDTO savedMovie = movieService.update(id, updatedMovieDTO);

        Assertions.assertNotNull(savedMovie);
        Assertions.assertEquals(updatedMovieDTO.getName(), savedMovie.getName());
        Assertions.assertEquals(updatedMovieDTO.getReleaseDate(), savedMovie.getReleaseDate());
    }

    @Test
    void should_deleteMovie() {

        when(movieRepository.findById(1L)).thenReturn(Optional.of(createdMovie));
        doNothing().when(movieRepository).deleteById(createdMovie.getId());

        movieService.delete(createdMovie.getId());

        verify(movieRepository, times(1)).deleteById(createdMovie.getId());
    }

    @Test
    void should_throwException_whenFindMovieByNotExistingId() {

        when(movieRepository.findById(-1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(MovieNotFoundException.class, () -> {
            movieService.getById(-1L);
        });
    }

    @Test
    void should_throwMovieAlreadyExists_whenUpdateMovieWhichAlreadyExists() {

        when(movieRepository.findByNameAndReleaseDate(mappedMovieForCreating.getName(), mappedMovieForCreating.getReleaseDate())).thenReturn(Optional.of(createdMovie));

        Assertions.assertThrows(MovieAlreadyExistsException.class, () -> {
            movieService.create(createdMovieDTO);
        });
    }

    @Test
    void should_throwException_whenDeleteMovieByNotExistingId() {

        when(movieRepository.findById(-1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(MovieNotFoundException.class, () -> {
            movieService.getById(-1L);
        });
    }

    @Test
    void should_throwException_whenUpdateMovieByNotExistingId() {

        when(movieRepository.findById(-1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(MovieNotFoundException.class, () -> {
            movieService.update(-1L, createdMovieDTO);
        });
    }

    @Test
    void should_returnSameDate_WhenPassingCreatedAt() {

        when(movieRepository.save(testingDate)).thenReturn(testingDate);

        Movie movie = movieRepository.save(testingDate);

        Assertions.assertEquals(testingDate.getCreatedAt(), movie.getCreatedAt());
    }

    @Test
    void should_returnSameModifiedAt_whenSaving() {

        when(movieRepository.save(testingDateUpdate)).thenReturn(testingDate);

        Movie movie = movieRepository.save(testingDateUpdate);

        Assertions.assertNotEquals(testingDateUpdate.getModifiedAt(), movie.getModifiedAt());
    }
}