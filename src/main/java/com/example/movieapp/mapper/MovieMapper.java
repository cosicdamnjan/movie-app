package com.example.movieapp.mapper;

import com.example.movieapp.dto.MovieDTO;
import com.example.movieapp.model.Movie;
import com.example.movieapp.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ActorMapper.class})
public interface MovieMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Movie mapToMovie(MovieDTO movieDTO);

    @Mapping(source = "movie", target = "avgRate", qualifiedByName = "calculateAvgRate")
    MovieDTO mapToMovieDTO(Movie movie);

    List<MovieDTO> mapToMovieDTOList(List<Movie> movieList);

    @Named("calculateAvgRate")
    default double calculateAvgRate(Movie movie) {

        double average = movie.getReviews().stream()
                .mapToInt(Review::getRating)
                .summaryStatistics()
                .getAverage();
        String format = String.format("%.2f", average);

        return Double.parseDouble(format);
    }
}