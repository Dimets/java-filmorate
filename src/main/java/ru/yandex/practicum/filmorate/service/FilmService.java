package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;
    private final UserService userService;
    private final FilmLikeDao filmLikeDao;
    private final DirectorService directorService;
    private final GenreDao genreDao;
    private final MpaDao mpaDao;


    public Film create(Film film) throws ValidationException, UnknownMpaException,
            UnknownGenreException, UnknownUserException, UnknownDirectorException {
        validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) throws ValidationException, UnknownFilmException, UnknownMpaException,
            UnknownGenreException, UnknownUserException, UnknownDirectorException {
        validateFilm(film);
        filmStorage.getFilmById(film.getId()).orElseThrow(() -> new UnknownFilmException(
                String.format("Фильм с id=%d не существует", film.getId())));
        log.info("Фильм с id={} изменен", film.getId());
        return filmStorage.updateFilm(film);
    }

    public void deleteById(int id) throws UnknownFilmException, UnknownMpaException, UnknownGenreException,
            UnknownUserException, UnknownDirectorException {
        filmStorage.getFilmById(id).orElseThrow(() -> new UnknownFilmException(
                String.format("Фильм с id=%d не существует", id)));
        log.info("Фильм с id={} удален", id);
        filmStorage.deleteFilm(id);
    }

    public List<Film> findAll() throws UnknownMpaException, UnknownGenreException, UnknownUserException, UnknownDirectorException {
        log.info("Список фильмов получен size=: " + filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film findById(int id) throws UnknownFilmException, UnknownMpaException, UnknownGenreException,
            UnknownUserException, UnknownDirectorException {
        return filmStorage.getFilmById(id).orElseThrow(() -> new UnknownFilmException(
                String.format("Фильм с id=%d не существует", id)));
    }

    public void addLike(int filmId, int userId) throws UnknownFilmException, UnknownUserException,
            UnknownMpaException, UnknownGenreException, UnknownDirectorException {
        checkExistFilmAndUser(filmId, userId);
        filmLikeDao.addFilmLike(filmId,userId);
        log.info(String.format("Лайк пользователя id=%d добавлен к фильму id=%d", userId, filmId));
    }

    public void deleteLike (int filmId, int userId) throws UnknownFilmException, UnknownUserException,
            UnknownMpaException, UnknownGenreException, UnknownDirectorException {
        checkExistFilmAndUser(filmId, userId);
        filmLikeDao.deleteFilmLike(filmId,userId);
        log.info(String.format("Лайк пользователя id=%d удален у фильма id=%d", userId, filmId));
    }

    public List<Film> findPopular(int count) throws UnknownMpaException, UnknownGenreException, UnknownUserException, UnknownDirectorException {
        return filmStorage.getPopular(count);
    }

    public List<Film> findPopular(Integer count, Optional<Integer> genreId, Optional<Integer> year) throws UnknownMpaException,
            UnknownGenreException, UnknownUserException, UnknownDirectorException {
        return filmStorage.getPopular(count, genreId, year);
    }

    public List<Film> getUserFilms(int userId) throws UnknownMpaException, UnknownFilmException, UnknownGenreException,
            UnknownUserException, UnknownDirectorException {
        List<Film> userFilms = new ArrayList<>();
        for (int i : filmLikeDao.getUserLikes(userId)) {
            userFilms.add(findById(i));
        }
        log.debug(String.format("Список фильмов пользователя id=%d: {}", userId, userFilms));
        return userFilms;
    }

    public List<Film> getCommonFilms(int userId, int friendId) throws UnknownMpaException, UnknownFilmException,
            UnknownGenreException, UnknownUserException, UnknownDirectorException {
        List<Film> result = new ArrayList<>();
        result.addAll(getUserFilms(userId));
        result.retainAll(getUserFilms(friendId));
        result.stream().sorted((film1,film2) ->film1.getLikes().size() - film2.getLikes().size())
                .collect(Collectors.toList());
        return result;
    }

    public List<Film> findPopularByDirector(int id, String sortBy) throws UnknownMpaException, UnknownGenreException, UnknownUserException, UnknownDirectorException {
        return filmStorage.getPopularByDirector(id, sortBy);
    }

    public void validateFilm(Film film) throws ValidationException, UnknownDirectorException, UnknownGenreException, UnknownMpaException {
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
        mpaDao.findMpaById(film.getMpa().getId());
        if (film.getGenres() != null) {
            for (Genre genre: film.getGenres()) {
                genreDao.findGenreById(genre.getId());
            }
        }
        if (film.getDirectors() != null) {
            for (Director director: film.getDirectors()) {
                    directorService.findById(director.getId());
            }
        }
    }

    void checkExistFilmAndUser(int filmId, int userId) throws UnknownFilmException, UnknownUserException,
            UnknownMpaException, UnknownGenreException, UnknownDirectorException {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new UnknownFilmException(
                String.format("Фильм с id=%d не существует", filmId)));
        userService.findById(userId);
    }
}
