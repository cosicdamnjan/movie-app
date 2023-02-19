package com.example.movieapp.dto;

import com.example.movieapp.model.Movie;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JsonDataDTO {

    private List<Movie> movies = new ArrayList<>();
}
