package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequestMapping("/directors")
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @PostMapping
    public Director create(@RequestBody Director director) throws ValidationException {
        log.info("POST /directors {}", director);
        return directorService.create(director);
    }

    @PutMapping()
    public Director update(@RequestBody Director director) throws ValidationException, EntityNotFoundException {
        log.info("PUT /directors {}", director);
        return directorService.update(director);
    }

    @DeleteMapping("/{directorId}")
    public void delete(@PathVariable("directorId") int directorId) throws EntityNotFoundException {
        log.info("DELETE /directors/" + directorId);
        directorService.deleteById(directorId);
    }

    @GetMapping
    public List<Director> findAll() {
        log.info("GET /directors");
        return directorService.findAll();
    }

    @GetMapping("/{directorId}")
    public Director findDirectorById(@PathVariable("directorId") int directorId) throws EntityNotFoundException {
        log.info("GET /directors/" + directorId);
        return directorService.findById(directorId);
    }
}
