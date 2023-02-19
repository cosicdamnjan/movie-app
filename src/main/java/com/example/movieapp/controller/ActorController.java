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
    public ResponseEntity<List<ActorDTO>> getAll() {

        List<ActorDTO> actors = actorService.findAll();

        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getById(@PathVariable Long id) {

        ActorDTO actorDTO = actorService.findById(id);

        return new ResponseEntity<>(actorDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ActorDTO> save(@RequestBody @Valid ActorDTO actorDTO) {

        ActorDTO actor = actorService.create(actorDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorDTO> update(@PathVariable Long id, @RequestBody @Valid ActorDTO actorDTO) {

        ActorDTO actor = actorService.update(id, actorDTO);

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {

        actorService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}