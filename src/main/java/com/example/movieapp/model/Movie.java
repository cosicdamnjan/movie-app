package com.example.movieapp.model;

import com.example.movieapp.model.enumeration.Genre;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Movie {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Name must not be blank.")
    private String name;
    @NotBlank(message = "Description must not be blank.")
    @Size(max = 1000, message = "Cannot have more than 1000 characters")
    private String description;
    @NotNull
    private Date releaseDate;
    @NotNull
    private Double durationInMin;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @Transient
    private Double avgRate;
    private List<Genre> genre;

    @Relationship(type = "ACTED_IN", direction = Relationship.Direction.INCOMING)
    private List<Actor> actors;

    @Relationship(type = "REVIEWED", direction = Relationship.Direction.OUTGOING)
    private List<Review> reviews;
}
