package com.example.movieapp.service.impl;

import com.example.movieapp.dto.ReviewDTO;
import com.example.movieapp.error.exceptions.ReviewNotFoundException;
import com.example.movieapp.mapper.ReviewMapper;
import com.example.movieapp.model.Review;
import com.example.movieapp.repository.ReviewRepository;
import com.example.movieapp.service.ReviewService;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.InvalidPathException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String NOT_FOUND = "Review not found";

    private final ReviewMapper mapper = Mappers.getMapper(ReviewMapper.class);

    private final ReviewRepository reviewRepository;

    @Override
    public ReviewDTO createReview(Long movieId, ReviewDTO reviewDTO) {

        Review reviewToSave = mapper.mapToReview(reviewDTO);

        Review review = reviewRepository.save(reviewToSave);

        return mapper.mapToReviewDTO(review);
    }

    @Override
    public void deleteReview(Long movieId, Long reviewId) {

        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review.isEmpty()) {
            throw new ReviewNotFoundException(NOT_FOUND);
        }

        Review reviewInMovie = reviewRepository.findReviewInMovie(movieId, reviewId);
        if (Objects.isNull(reviewInMovie)) {
            throw new InvalidPathException("Movie with id  " + movieId + " is not attached to review with id " + reviewId, NOT_FOUND);
        }

        reviewRepository.deleteById(reviewId);
    }

    @Override
    public List<ReviewDTO> findReviewsByMovieId(Long movieId) {

        List<Review> reviews = reviewRepository.findReviewsByMovieId(movieId);

        return mapper.mapToReviewDTOList(reviews);
    }
}