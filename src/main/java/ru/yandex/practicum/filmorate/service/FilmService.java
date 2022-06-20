package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.UnknownFilmException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final UserService userService;

    public Film create(Film film) throws ValidationException {
        validateFilm(film);
        film.setId(Film.getNextId());
        inMemoryFilmStorage.createFilm(film);
        log.info("Фильм создан: {}", film.getId());
        log.debug("Фильм создан: {}", film);
        return film;
    }

    public Film update(Film film) throws ValidationException, UnknownFilmException {
        validateFilm(film);
        if (inMemoryFilmStorage.getFilmById(film.getId()) != null) {
            inMemoryFilmStorage.updateFilm(film);
            log.info("Фильм изменен: {}", film.getId());
            log.debug("Фильм изменен: {}", film);
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

    public void addLike(int filmId, int userId) throws UnknownFilmException, UnknownUserException {
        checkExistFilmAndUser(filmId, userId);
        inMemoryFilmStorage.getFilmById(filmId).getLikes().add(userId);
        log.info(String.format("Лайк пользователя id=%d добавлен к фильму id=%d", userId, filmId));
    }

    public void deleteLike (int filmId, int userId) throws UnknownFilmException, UnknownUserException {
        checkExistFilmAndUser(filmId, userId);
        inMemoryFilmStorage.getFilmById(filmId).getLikes().remove(userId);
        log.info(String.format("Лайк пользователя id=%d удален у фильма id=%d", userId, filmId));
    }

    public List<Film> findPopular(int count) {
        List<Film> popularFilms = inMemoryFilmStorage.getAllFilms();
        Collections.sort(popularFilms, (o1, o2) -> o2.getLikes().size() - o1.getLikes().size());
        return popularFilms.subList(0, count > popularFilms.size() ? popularFilms.size() : count);
    }

    public void validateFilm(Film film) throws ValidationException {
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

    void checkExistFilmAndUser(int filmId, int userId) throws UnknownFilmException, UnknownUserException {
        if (inMemoryFilmStorage.getFilmById(filmId) == null) {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", filmId));
        }
        if (userService.findById(userId) == null) {
            throw new UnknownUserException(String.format("Пользователь с id=%d не существует", userId));
        }
    }

}
