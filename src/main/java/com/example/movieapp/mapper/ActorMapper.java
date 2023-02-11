package com.example.movieapp.mapper;

import com.example.movieapp.dto.ActorDTO;
import com.example.movieapp.model.Actor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Actor mapToActor(ActorDTO actorDTO);

    ActorDTO mapToActorDTO(Actor actor);

    List<ActorDTO> mapToActorDTOList(List<Actor> actorList);
}
