package com.example.movieapp.controller;

import com.example.movieapp.dto.ActorDTO;
import com.example.movieapp.service.ActorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    @GetMapping
    public ResponseEntity<List<ActorDTO>> getAllActors() {
        List<ActorDTO> actorDTOS = actorService.findAll();
        return new ResponseEntity<>(actorDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getActorById(@PathVariable Long id) {
        ActorDTO actorDTO = actorService.findById(id);
        return new ResponseEntity<>(actorDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActorDTO> createActor(@RequestBody @Valid ActorDTO actorDTO) {
        actorService.create(actorDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorDTO> updateActor(@PathVariable Long id, @RequestBody @Valid ActorDTO actorDTO) {
        ActorDTO actor = actorService.update(id, actorDTO);
        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteActor(@PathVariable Long id) {
        actorService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
