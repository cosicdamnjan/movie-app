package com.example.movieapp.mapper;

import com.example.movieapp.dto.ReviewDTO;
import com.example.movieapp.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Review mapToReview(ReviewDTO reviewDTO);

    ReviewDTO mapToReviewDTO(Review review);

    List<ReviewDTO> mapToReviewDTOList(List<Review> reviewList);
}
