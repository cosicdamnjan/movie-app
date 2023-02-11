package com.example.movieapp.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Actor {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotNull
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
    @NotBlank(message = "Biography is mandatory")
    @Size(max = 1000, message = "Cannot have more characters than 1000.")
    private String biography;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
