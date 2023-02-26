package com.prodyna.movieapp.mapper;

import com.prodyna.movieapp.domain.Actor;
import com.prodyna.movieapp.dto.ActorDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ActorMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    Actor mapToActor(ActorDTO actorDTO);

    ActorDTO mapToActorDTO(Actor actor);

    List<ActorDTO> mapToActorDTOList(List<Actor> actorList);
}
