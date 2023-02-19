package com.example.movieapp.service;

import com.example.movieapp.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {

    ReviewDTO createReview(Long movieId, ReviewDTO reviewDTO);

    void deleteReview(Long movieId, Long reviewId);

    List<ReviewDTO> findReviewsByMovieId(Long movieId);
}
