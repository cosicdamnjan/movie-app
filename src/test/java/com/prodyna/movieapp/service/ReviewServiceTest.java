package com.prodyna.movieapp.service;

import com.prodyna.movieapp.domain.Review;
import com.prodyna.movieapp.dto.ReviewDTO;
import com.prodyna.movieapp.error.exceptions.ReviewNotFoundException;
import com.prodyna.movieapp.mapper.ReviewMapper;
import com.prodyna.movieapp.repository.ReviewRepository;
import com.prodyna.movieapp.service.impl.MovieServiceImpl;
import com.prodyna.movieapp.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewMapper mapper;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewDTO createReviewDTO;
    private Review createReview, updateReview;

    @BeforeEach
    private void setUp() {


        createReviewDTO = ReviewDTO.builder()
                .id(2L)
                .title("Pretty good")
                .rating(1)
                .description("Pretty good movie, watch it")
                .build();

        createReview = Review.builder()
                .id(2L)
                .title("Pretty good")
                .rating(1)
                .description("Pretty good movie, watch it")
                .build();

        updateReview = Review.builder()
                .id(2L)
                .title("Pretty good")
                .rating(5)
                .description("Good movie, watch it")
                .build();
    }


    @Test
    void should_createReview() {

        lenient().when(mapper.mapToReview(createReviewDTO)).thenReturn(createReview);
        when(reviewRepository.save(createReview)).thenReturn(createReview);

        lenient().when(mapper.mapToReviewDTO(createReview)).thenReturn(createReviewDTO);

        Assertions.assertDoesNotThrow(() -> reviewService.createReview(1L, createReviewDTO));

        ReviewDTO review = reviewService.createReview(1L, createReviewDTO);

        Assertions.assertNotNull(review);
    }

    @Test
    public void should_deleteReview() {

        when(reviewRepository.findById(createReview.getId())).thenReturn(Optional.of(createReview));
        when(reviewRepository.findReviewInMovie(1L, createReview.getId()))
                .thenReturn(createReview);
        doNothing().when(reviewRepository).deleteById(createReview.getId());

        reviewService.deleteReview(1L, createReview.getId());
        verify(reviewRepository, times(1)).deleteById(createReview.getId());
    }

    @Test
    public void should_findReviewById() {

        lenient().when(reviewRepository.findReviewInMovie(1L, createReview.getId()))
                .thenReturn(createReview);
        lenient().when(mapper.mapToReviewDTO(createReview)).thenReturn(createReviewDTO);

        List<ReviewDTO> reviews = reviewService.findReviewsByMovieId(1L);

        Assertions.assertNotNull(reviews);
    }

    @Test
    public void should_throwException_whenDeleteReviewByNotExistingId() {

        when(reviewRepository.findById(-1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () -> {
            reviewService.deleteReview(1L, -1L);
        });
    }

    @Test
    public void should_throwException_whenDeleteReviewNotAttachedToMovie() {

        when(reviewRepository.findById(2L)).thenReturn(Optional.of(createReview));
        when(reviewRepository.findReviewInMovie(1L, createReview.getId()))
                .thenReturn(null);

        Assertions.assertThrows(InvalidPathException.class, () -> {
            reviewService.deleteReview(1L, 2L);
        });
    }
}