package com.example.movieapp.repository;

import com.example.movieapp.model.Actor;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActorRepository extends Neo4jRepository<Actor, Long> {

    Optional<Actor> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("OPTIONAL MATCH (n:Movie)<-[:ACTED_IN]-(a:Actor) WHERE id(n)=$id return collect(a)")
    List<Actor> findActorsByMovieId(@Param("id") Long id);
}
