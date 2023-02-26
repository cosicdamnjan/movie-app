package com.prodyna.movieapp.service.impl;

import com.prodyna.movieapp.domain.Actor;
import com.prodyna.movieapp.dto.ActorDTO;
import com.prodyna.movieapp.error.exceptions.ActorAlreadyExistsException;
import com.prodyna.movieapp.error.exceptions.ActorNotFoundException;
import com.prodyna.movieapp.mapper.ActorMapper;
import com.prodyna.movieapp.repository.ActorRepository;
import com.prodyna.movieapp.service.ActorService;
import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.InvalidPathException;
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
        List<Actor> actorList = actorRepository.findAll(
                Sort.by(Sort.Direction.ASC, "firstName"));
        return mapper.mapToActorDTOList(actorList);
    }

    @Override
    public ActorDTO findById(Long id) {
        Actor actor = actorRepository.findById(id).orElseThrow(
                () -> new ActorNotFoundException(NOT_FOUND));
        return mapper.mapToActorDTO(actor);
    }

    @Override
    public ActorDTO create(ActorDTO actorDTO) {
        actorRepository.findByFirstNameAndLastName(actorDTO.getFirstName(), actorDTO.getLastName())
                .ifPresent(actorExists -> {
                    throw new ActorAlreadyExistsException(ALREADY_EXISTS);
                });

        Actor actor = mapper.mapToActor(actorDTO);
        Actor savedActor = actorRepository.save(actor);

        return mapper.mapToActorDTO(savedActor);
    }

    @Override
    public ActorDTO update(Long id, ActorDTO actorDTO) {
        Actor actorDB = actorRepository.findById(id).orElseThrow(
                () -> new ActorNotFoundException(NOT_FOUND));

        Optional<Actor> optionalActor = actorRepository.findByFirstNameAndLastName(actorDTO.getFirstName(), actorDTO.getLastName());

        if (optionalActor.isPresent() && !Objects.equals(optionalActor.get().getId(), actorDB.getId())) {
            throw new ActorAlreadyExistsException(ALREADY_EXISTS);
        }

        actorDB.setFirstName(actorDTO.getFirstName());
        actorDB.setLastName(actorDTO.getLastName());
        actorDB.setBiography(actorDTO.getBiography());

        actorRepository.save(actorDB);
        return mapper.mapToActorDTO(actorDB);
    }

    @Override
    public void delete(Long id) {
        Actor actor = actorRepository.findById(id).orElseThrow(() -> new ActorNotFoundException(NOT_FOUND));
        actorRepository.deleteById(actor.getId());
    }

    @Override
    public List<ActorDTO> findActorsByMovieId(Long movieId) {
        List<Actor> actors = actorRepository.findActorsByMovieId(movieId);
        return mapper.mapToActorDTOList(actors);
    }

    @Override
    public void addActorToMovie(Long movieId, ActorDTO actorDTO) {
        Actor actor = mapper.mapToActor(actorDTO);
        Optional<Actor> actorExist = actorRepository.findByFirstNameAndLastName(actor.getFirstName(), actor.getLastName());
        actor = actorExist.isPresent() ? actorExist.get() : actorRepository.save(actor);

        actorRepository.addActorToMovie(movieId, actor.getId());
    }

    @Override
    public void deleteActorFromMovie(Long id, Long actorId) {
        Optional<Actor> actor = actorRepository.findById(actorId);
        if (actor.isEmpty()) {
            throw new ActorNotFoundException(NOT_FOUND);
        }

        Actor actorInMovie = actorRepository.findActorInMovie(id, actorId);
        if (Objects.isNull(actorInMovie)) {
            throw new InvalidPathException("Movie with id  " + id + " is not attached to actor with id " + actorId, NOT_FOUND);
        }

        actorRepository.deleteActorFromMovie(id, actorId);
    }
}