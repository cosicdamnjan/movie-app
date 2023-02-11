package com.example.movieapp.service.impl;

import com.example.movieapp.dto.ActorDTO;
import com.example.movieapp.mapper.ActorMapper;
import com.example.movieapp.model.Actor;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.service.ActorService;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ActorServiceImpl implements ActorService {

    private static final String NOT_FOUND = "Actor not found";
    private static final String ALREADY_EXISTS = "Actor already exists";
    private final ActorRepository actorRepository;
    private final ActorMapper mapper = Mappers.getMapper(ActorMapper.class);


    @Override
    public List<ActorDTO> findAll() {
        List<Actor> actorList = actorRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName"));
        return mapper.mapToActorDTOList(actorList);
    }

    @Override
    public ActorDTO findById(Long id) {
        Actor actor = actorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(NOT_FOUND));
        return mapper.mapToActorDTO(actor);
    }

    @Override
    public ActorDTO create(ActorDTO actorDTO) {
        actorRepository.findByFirstNameAndLastName(actorDTO.getFirstName(), actorDTO.getLastName())
                .ifPresent(actor -> {
                    throw new IllegalStateException(ALREADY_EXISTS);
                });

        Actor actor = mapper.mapToActor(actorDTO);
        Actor savedActor = actorRepository.save(actor);
        return mapper.mapToActorDTO(savedActor);
    }

    @Override
    public ActorDTO update(Long id, ActorDTO actorDTO) {
        Actor actorDB = actorRepository.findById(id).orElseThrow(
                () -> new NotFoundException(NOT_FOUND));

        Optional<Actor> optionalActor = actorRepository.findByFirstNameAndLastName(
                actorDTO.getFirstName(), actorDTO.getLastName());

        if (optionalActor.isPresent() && !Objects.equals(optionalActor.get().getId(), actorDB.getId())) {
            throw new IllegalStateException(ALREADY_EXISTS);
        }

        actorDB.setFirstName(actorDTO.getFirstName());
        actorDB.setLastName(actorDTO.getLastName());
        actorDB.setBiography(actorDTO.getBiography());

        actorRepository.save(actorDB);

        return mapper.mapToActorDTO(actorDB);
    }

    @Override
    public void delete(Long id) {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new NotFoundException(NOT_FOUND));
        actorRepository.deleteById(actor.getId());
    }

    @Override
    public List<ActorDTO> findActorsByMovieId(Long movieId) {
        List<Actor> actors = actorRepository.findActorsByMovieId(movieId);
        return mapper.mapToActorDTOList(actors);
    }
}
