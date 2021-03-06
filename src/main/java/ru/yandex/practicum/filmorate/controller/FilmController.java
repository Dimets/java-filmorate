package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
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
    public Film create(@RequestBody Film film) throws ValidationException, UnknownFilmException, UnknownMpaException,
            UnknownGenreException, UnknownUserException {
        log.info("POST /films {}", film);
        filmService.create(film);
        return filmService.findById(film.getId());
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException, UnknownFilmException,
            UnknownMpaException, UnknownGenreException, UnknownUserException {
        log.info("PUT /films id={}", film.getId());
        log.debug("PUT /films {}", film);
        return filmService.update(film);
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("filmId") int filmId) throws UnknownFilmException,
            UnknownMpaException, UnknownGenreException, UnknownUserException {
        log.info("DELETE /films/" + filmId);
        filmService.deleteById(filmId);
    }

    @GetMapping
    public List<Film> findAll() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        log.info("GET /films");
        return filmService.findAll();
    }

    @GetMapping("/{filmId}")
    public Film findFilm(@PathVariable("filmId") int filmId) throws UnknownFilmException, UnknownMpaException,
            UnknownGenreException, UnknownUserException {
        log.info("GET /films/" + filmId);
        return filmService.findById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws UnknownFilmException, UnknownUserException, UnknownMpaException, UnknownGenreException {
        log.info("PUT /" + filmId + "/like/" + userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLike(@PathVariable("filmId") int filmId, @PathVariable("userId") int userId)
            throws UnknownFilmException, UnknownUserException, UnknownMpaException, UnknownGenreException {
        log.info("DELETE /" + filmId + "/like/" + userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") Integer count)
            throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        log.info("GET /popular count = {}", count);
        return filmService.findPopular(count);
    }
}
