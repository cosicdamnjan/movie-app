package com.prodyna.movieapp.repository;

import com.prodyna.movieapp.domain.Review;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends Neo4jRepository<Review, Long> {

    @Query("OPTIONAL MATCH (n:Movie)-[:REVIEWED]->(r:Review) WHERE id(n)=$id return collect(r)")
    List<Review> findReviewsByMovieId(@Param("id") Long id);
    @Query("OPTIONAL MATCH (m:Movie)-[:REVIEWED]->(r:Review) where id(m)=$movieId and id(r)=$reviewId return r")
    Review findReviewInMovie(@Param("movieId") Long movieId, @Param("reviewId") Long reviewId);

}
