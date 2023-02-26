package com.prodyna.movieapp.dto;

import com.prodyna.movieapp.domain.Movie;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonDataDTO {

    private List<Movie> movies = new ArrayList<>();
}
