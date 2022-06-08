package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        log.info("POST /films {}", film);

        if (validate(film)) {
            films.put(film.getId(), film);
            log.info("Фильм создан: {}", film);
        }
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws UnknownFilmException, ValidationException {
        log.info("PUT /films {}", film);
        if (films.containsKey(film.getId())) {
            if (validate(film)) {
                films.put(film.getId(), film);
            }
        } else {
            throw new UnknownFilmException("Фильма с таким id не существует!");
        }
        log.info("Фильм изменен: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("GET /films");
        return new ArrayList<>(films.values());
    }

    public static boolean validate(Film film) throws ValidationException {
        if (!StringUtils.hasText(film.getName())) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (StringUtils.hasText(film.getDescription()) && film.getDescription().length() > 200) {
            throw  new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        return true;
    }
}
