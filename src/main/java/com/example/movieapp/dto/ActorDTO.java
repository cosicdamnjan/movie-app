package com.example.movieapp.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActorDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
}
