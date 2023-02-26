package com.prodyna.movieapp.repository;

import com.prodyna.movieapp.domain.Movie;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {

    Optional<Movie> findByNameAndReleaseDate(String name, Date releaseDate);

    @Query(value = "MATCH (m:Movie) WHERE m.name CONTAINS $name " +
            "AND any(genres IN m.genre WHERE genres CONTAINS toUpper($genre)) " +
            "OPTIONAL MATCH (m)<-[a:ACTED_IN]-(actor:Actor) OPTIONAL MATCH (m)-[r:REVIEWED]->(review:Review) " +
            "WITH DISTINCT (m), collect(a) as act, collect(r) as rev, collect(actor) as actors, collect(review) as reviews " +
            "RETURN m, act, rev, actors, reviews SKIP $skip LIMIT $limit",
            countQuery = "MATCH (m:Movie) WHERE m.name CONTAINS $name " +
                    "AND any(genres IN m.genre WHERE genres CONTAINS toUpper($genre)) RETURN count(m)")
    Page<Movie> searchMoviesByNameOrGenre(String name, String genre, Pageable paging);
}
