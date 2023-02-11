package com.example.movieapp.service;

import com.example.movieapp.dto.ActorDTO;

import java.util.List;

public interface ActorService {

    List<ActorDTO> findAll();

    ActorDTO findById(Long id);

    ActorDTO create(ActorDTO actorDTO);

    ActorDTO update(Long id, ActorDTO actorDTO);

    void delete(Long id);
}
