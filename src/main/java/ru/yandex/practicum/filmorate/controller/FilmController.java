package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        log.info("POST /films {}", film);
        filmService.create(film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, UnknownFilmException {
        log.info("PUT /films {}", film);
        filmService.update(film);
        return film;
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("filmId") int filmId) throws UnknownFilmException {
        log.info("DELETE /films/" + filmId);
        filmService.deleteById(filmId);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("GET /films");
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") int filmId) throws UnknownFilmException {
        log.info("GET /films/" + filmId);
        return filmService.findById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws UnknownFilmException, UnknownUserException {
        log.info("PUT /" + filmId + "/like/" + userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws UnknownFilmException, UnknownUserException {
        log.info("DELETE /" + filmId + "/like/" + userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmService.findPopular(count);
    }
}
