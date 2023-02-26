package com.prodyna.movieapp.repository;

import com.prodyna.movieapp.domain.Actor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends Neo4jRepository<Actor, Long> {

    Optional<Actor> findByFirstNameAndLastName(String firstName, String lastName);

    @Query("OPTIONAL MATCH (n:Movie)<-[:ACTED_IN]-(a:Actor) WHERE id(n)=$id return collect(a)")
    List<Actor> findActorsByMovieId(@Param("id") Long id);

    @Query("MATCH (m:Movie), (a:Actor) where id(m)=$movieId and id(a)=$actorId MERGE (m)-[:ACTED_IN]->(a)")
    void addActorToMovie(@Param("movieId") Long movieId, @Param("actorId") Long actorId);

    @Query("MATCH (m:Movie)<-[r:ACTED_IN]-(a:Actor) where id(m)=$movieId and id(a)=$actorId delete r")
    void deleteActorFromMovie(@Param("movieId") Long id, @Param("actorId") Long actorId);

    @Query("OPTIONAL MATCH (m:Movie)<-[:ACTED_IN]-(a:Actor) where id(m)=$movieId and id(a)=$actorId return a")
    Actor findActorInMovie(@Param("movieId") Long id, @Param("actorId") Long actorId);
}