package com.example.movieapp.dto;

import com.example.movieapp.model.enumeration.Genre;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MovieDTO {

    private Long id;
    private String name;
    private List<Genre> genre;
    private String description;
    private Date releaseDate;
    private Double durationInMin;
    private Double avgRate;
    private List<ActorDTO> actors = new ArrayList<>();
    private List<ReviewDTO> reviews = new ArrayList<>();
}
