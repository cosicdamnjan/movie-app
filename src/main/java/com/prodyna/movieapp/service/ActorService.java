package com.prodyna.movieapp.service;

import com.prodyna.movieapp.dto.ActorDTO;
import java.util.List;

public interface ActorService {

    List<ActorDTO> findAll();

    ActorDTO findById(Long id);

    ActorDTO create(ActorDTO actorDTO);

    ActorDTO update(Long id, ActorDTO actorDTO);

    void delete(Long id);

    List<ActorDTO> findActorsByMovieId(Long movieId);

    void addActorToMovie(Long movieId, ActorDTO actorDTO);

    void deleteActorFromMovie(Long id, Long actorId);
}
