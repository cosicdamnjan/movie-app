package com.prodyna.movieapp.domain;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Review {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Size(min = 1, max = 5, message = "Rating must be between 1 and 5.")
    private Integer rating;
    @NotBlank(message = "Title must be not empty")
    @Size(min = 3, max = 100, message = "Characters must be between 3 and 100.")
    private String title;
    @NotBlank(message = "Must not be empty")
    @Size(max = 1000, message = "Description must be not empty")
    private String description;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
