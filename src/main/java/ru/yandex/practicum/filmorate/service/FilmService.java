package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film create(Film film) throws ValidationException {
        validateFilm(film);
        inMemoryFilmStorage.createFilm(film);
        log.info("Фильм создан: {}", film);
        return film;
    }

    public Film update(Film film) throws ValidationException, UnknownFilmException {
        validateFilm(film);
        if (inMemoryFilmStorage.getFilmById(film.getId()) != null) {
            inMemoryFilmStorage.updateFilm(film);
            log.info("Фильм изменен: {}", film);
            return film;
        } else {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", film.getId()));
        }
    }

    public void deleteById(int id) throws UnknownFilmException {
        if (inMemoryFilmStorage.getFilmById(id) != null) {
            log.info(String.format("Фильм с id=%d удален:", id) + inMemoryFilmStorage.getFilmById(id));
            inMemoryFilmStorage.deleteFilm(id);
        } else {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", id));
        }
    }

    public List<Film> findAll() {
        log.info("Список фильмов получен: " + inMemoryFilmStorage.getAllFilms());
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film findById(int id) throws UnknownFilmException {
        if (inMemoryFilmStorage.getFilmById(id) != null) {
            log.info(String.format("Фильм с id=%d найден:", id) + inMemoryFilmStorage.getFilmById(id));
            return inMemoryFilmStorage.getFilmById(id);
        } else {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", id));
        }
    }


    void validateFilm(Film film) throws ValidationException {
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
    }

}
