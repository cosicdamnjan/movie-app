package com.prodyna.movieapp.domain;

import com.prodyna.movieapp.enumeration.Genre;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

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
