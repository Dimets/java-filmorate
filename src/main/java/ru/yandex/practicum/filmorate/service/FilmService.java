package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;
    private final UserService userService;
    private final FilmLikeDao filmLikeDao;


    public Film create(Film film) throws ValidationException, UnknownMpaException,
            UnknownGenreException, UnknownUserException {
        validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) throws ValidationException, UnknownFilmException, UnknownMpaException,
            UnknownGenreException, UnknownUserException {
        validateFilm(film);
        if (filmStorage.getFilmById(film.getId()) != null) {
            log.info("Фильм изменен: {}", film.getId());
            log.debug("Фильм изменен: {}", film);
            return filmStorage.updateFilm(film);
        } else {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", film.getId()));
        }
    }

    public void deleteById(int id) throws UnknownFilmException, UnknownMpaException, UnknownGenreException,
            UnknownUserException {
        if (filmStorage.getFilmById(id) != null) {
            log.info(String.format("Фильм с id=%d удален:", id) + filmStorage.getFilmById(id));
            filmStorage.deleteFilm(id);
        } else {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", id));
        }
    }

    public List<Film> findAll() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        log.info("Список фильмов получен size=: " + filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film findById(int id) throws UnknownFilmException, UnknownMpaException, UnknownGenreException,
            UnknownUserException {
        if (filmStorage.getFilmById(id) != null) {
            log.info(String.format("Фильм с id=%d найден:", id));
            log.debug(String.format("Фильм с id=%d найден:", id) + filmStorage.getFilmById(id));
            return filmStorage.getFilmById(id);
        } else {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", id));
        }
    }

    public void addLike(int filmId, int userId) throws UnknownFilmException, UnknownUserException,
            UnknownMpaException, UnknownGenreException {
        checkExistFilmAndUser(filmId, userId);
        filmLikeDao.addFilmLike(filmId,userId);
        log.info(String.format("Лайк пользователя id=%d добавлен к фильму id=%d", userId, filmId));
    }

    public void deleteLike (int filmId, int userId) throws UnknownFilmException, UnknownUserException,
            UnknownMpaException, UnknownGenreException {
        checkExistFilmAndUser(filmId, userId);
        filmLikeDao.deleteFilmLike(filmId,userId);
        log.info(String.format("Лайк пользователя id=%d удален у фильма id=%d", userId, filmId));
    }

    public List<Film> findPopular(int count) throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        List<Film> popularFilms = filmStorage.getAllFilms();
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

    void checkExistFilmAndUser(int filmId, int userId) throws UnknownFilmException, UnknownUserException,
            UnknownMpaException, UnknownGenreException {
        if (filmStorage.getFilmById(filmId) == null) {
            throw new UnknownFilmException(String.format("Фильм с id=%d не существует", filmId));
        }
        if (userService.findById(userId) == null) {
            throw new UnknownUserException(String.format("Пользователь с id=%d не существует", userId));
        }
    }



}
