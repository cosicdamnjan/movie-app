package com.prodyna.movieapp.service;

import com.prodyna.movieapp.domain.Actor;
import com.prodyna.movieapp.dto.ActorDTO;
import com.prodyna.movieapp.error.exceptions.ActorAlreadyExistsException;
import com.prodyna.movieapp.error.exceptions.ActorNotFoundException;
import com.prodyna.movieapp.repository.ActorRepository;
import com.prodyna.movieapp.service.impl.ActorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;
    @InjectMocks
    private ActorServiceImpl actorService;

    private Actor createdActor;
    private Actor createdSecondActor;
    private ActorDTO createdActorDTO;
    private Actor mappedActor;
    private ActorDTO createdSecondActorDTO;
    private Actor updatedActor;

    private Actor buildActor(Long id, String firstName, String lastName, String biography) {
        return Actor.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .biography(biography)
                .build();
    }

    private ActorDTO buildActorDTO(Long id, String firstName, String lastName, String biography) {
        return ActorDTO.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .biography(biography)
                .build();
    }

    @BeforeEach
    private void setUp() {

        createdActor = buildActor(1L, "Robert", "Pattinson", "Robert Douglas Thomas Pattinson is an English actor.");

        createdActorDTO = buildActorDTO(1L, "Robert", "Pattinson", "Robert Douglas Thomas Pattinson is an English actor.");

        mappedActor = buildActor(1L, "Robert", "Pattinson", "Robert Douglas Thomas Pattinson is an English actor.");

        updatedActor = buildActor(2L,  "Robert", "Pattinson", "Robert Douglas Thomas Pattinson is an English actor.");

        createdSecondActor = buildActor(2L, "Mark", "Hamill", "Mark Richard Hamill is an American actor, voice actor and writer.");

        createdSecondActorDTO = buildActorDTO(2L, "Mark", "Hamill", "Mark Richard Hamill is an American actor, voice actor and writer.");
    }

    @Test
    void should_createActor() {

        when(actorRepository.findByFirstNameAndLastName(
                createdActor.getFirstName(),
                createdActor.getLastName()))
                .thenReturn(Optional.empty());

        when(actorRepository.save(mappedActor)).thenReturn(createdActor);

        Assertions.assertDoesNotThrow(() -> actorService.create(createdActorDTO));

        ActorDTO savedActor = actorService.create(createdActorDTO);

        Assertions.assertEquals("Robert", savedActor.getFirstName());
        Assertions.assertEquals("Pattinson", savedActor.getLastName());
        Assertions.assertEquals(1L, savedActor.getId());
        Assertions.assertNotNull(savedActor);
    }

    @Test
    void should_updateActor() {

        when(actorRepository.findById(createdActor.getId())).thenReturn(Optional.of(createdActor));

        Assertions.assertDoesNotThrow(() -> {
            actorService.update(createdActorDTO.getId(), createdActorDTO);
        });

        ActorDTO actor = actorService.update(createdActor.getId(), createdActorDTO);

        Assertions.assertNotNull(actor);
        Assertions.assertEquals(createdActor.getId(), actor.getId());
    }

    @Test
    void should_findActorById() {

        when(actorRepository.findById(1L)).thenReturn(Optional.of(createdActor));

        ActorDTO actor = actorService.findById(1L);

        Assertions.assertNotNull(actor);
        Assertions.assertEquals(createdActorDTO.getId(), actor.getId());
    }

    @Test
    void should_deleteActor() {

        when(actorRepository.findById(1L)).thenReturn(Optional.of(createdActor));

        doNothing().when(actorRepository).deleteById(createdActor.getId());

        actorService.delete(createdActor.getId());

        verify(actorRepository, times(1)).deleteById(createdActor.getId());
    }

    @Test
    void should_throwActorAlreadyExists_whenSaveActorWhichAlreadyExists() {

        when(actorRepository.findByFirstNameAndLastName(
                mappedActor.getFirstName(),
                mappedActor.getLastName()))
                .thenReturn(Optional.of(createdActor));

        Assertions.assertThrows(ActorAlreadyExistsException.class, () -> {
            actorService.create(createdActorDTO);
        });
    }

    @Test
    void should_throwActorAlreadyExists_whenUpdateActorWhichAlreadyExists() {

        when(actorRepository.findById(createdActor.getId())).
                thenReturn(Optional.empty());

        Assertions.assertThrows(ActorNotFoundException.class, () -> {
            actorService.update(createdActor.getId(), createdActorDTO);
        });
    }

    @Test
    void should_throwActorNotFoundException_whenDeleteActorByNonExistingId() {

        when(actorRepository.findById(createdActor.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ActorNotFoundException.class, () -> {
            actorService.delete(createdActorDTO.getId());
        });
    }

    @Test
    void should_throwActorAlreadyExists_whenIsPresentWithDifferentId() {
        Long id = createdActorDTO.getId();

        when(actorRepository.findById(createdActor.getId())).thenReturn(Optional.ofNullable(createdActor));
        when(actorRepository.findByFirstNameAndLastName(updatedActor.getFirstName(), updatedActor.getLastName()))
                        .thenReturn(Optional.of(updatedActor));
        Assertions.assertThrows(ActorAlreadyExistsException.class, () -> {
            actorService.update(id, createdActorDTO);
        }, "should throw but didn't");
    }
}