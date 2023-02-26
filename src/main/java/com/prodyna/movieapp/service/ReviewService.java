package com.prodyna.movieapp.service;

import com.prodyna.movieapp.dto.ReviewDTO;
import java.util.List;

public interface ReviewService {

    ReviewDTO createReview(Long movieId, ReviewDTO reviewDTO);

    void deleteReview(Long movieId, Long reviewId);

    List<ReviewDTO> findReviewsByMovieId(Long movieId);
}