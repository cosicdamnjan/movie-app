package com.prodyna.movieapp.mapper;

import com.prodyna.movieapp.domain.Review;
import com.prodyna.movieapp.dto.ReviewDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Review mapToReview(ReviewDTO reviewDTO);

    ReviewDTO mapToReviewDTO(Review review);

    List<ReviewDTO> mapToReviewDTOList(List<Review> reviewList);
}
