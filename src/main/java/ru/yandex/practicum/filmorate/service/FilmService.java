package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
    @Autowired
    private final FeedDao feedDao;

    public Film create(Film film) throws ValidationException, EntityNotFoundException {
        validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film update(Film film) throws ValidationException, EntityNotFoundException {
        validateFilm(film);
        filmStorage.getFilmById(film.getId()).orElseThrow(() -> new EntityNotFoundException(
                String.format("Фильм с id=%d не существует", film.getId())));
        log.info("Фильм с id={} изменен", film.getId());
        return filmStorage.updateFilm(film);
    }

    public void deleteById(int id) throws EntityNotFoundException {
        filmStorage.getFilmById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Фильм с id=%d не существует", id)));
        log.info("Фильм с id={} удален", id);
        filmStorage.deleteFilm(id);
    }

    public List<Film> findAll() throws EntityNotFoundException {
        log.info("Список фильмов получен size=: " + filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film findById(int id) throws EntityNotFoundException {
        return filmStorage.getFilmById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Фильм с id=%d не существует", id)));
    }

    public void addLike(int filmId, int userId) throws EntityNotFoundException {
        checkExistFilmAndUser(filmId, userId);
        filmLikeDao.addFilmLike(filmId, userId);
        feedDao.createFeed(new Feed("LIKE", "ADD",(long) filmId, userId,
                Instant.now().toEpochMilli()));
        log.info(String.format("Лайк пользователя id=%d добавлен к фильму id=%d", userId, filmId));
    }

    public void deleteLike(int filmId, int userId) throws EntityNotFoundException {
        checkExistFilmAndUser(filmId, userId);
        filmLikeDao.deleteFilmLike(filmId, userId);
        feedDao.createFeed(new Feed("LIKE", "REMOVE",(long) filmId, userId,
                Instant.now().toEpochMilli()));
        log.info(String.format("Лайк пользователя id=%d удален у фильма id=%d", userId, filmId));
    }

    public List<Film> findPopular(int count) throws EntityNotFoundException {
        return filmStorage.getPopular(count);
    }

    public List<Film> searchFilms(String query, String by) throws EntityNotFoundException {
        String[] searchByArr = by.split(",");
        List<Film> foundFilms = new ArrayList<>();
        for (String nextBy : searchByArr) {
            if (nextBy.equalsIgnoreCase("director")) {
                foundFilms.addAll(filmStorage.searchFilmByDirector(query));
            } else if (nextBy.equalsIgnoreCase("title")) {
                foundFilms.addAll(filmStorage.searchFilmByTitle(query));
            } else {
                throw new EntityNotFoundException(String.format("невозможно искать по параметру %s.", nextBy));
            }
        }
        Collections.sort(foundFilms, (film1, film2) -> {
            int comp = film2.getLikes().size() - film1.getLikes().size();
            return comp;
        });

        return foundFilms;
    }

    public List<Film> findPopular(Integer count, Optional<Integer> genreId, Optional<Integer> year) throws EntityNotFoundException {
        return filmStorage.getPopular(count, genreId, year);
    }

    public List<Film> getUserFilms(int userId) throws EntityNotFoundException {
        List<Film> userFilms = new ArrayList<>();
        for (int i : filmLikeDao.getUserLikes(userId)) {
            userFilms.add(findById(i));
        }
        log.debug(String.format("Список фильмов пользователя id=%d: {}", userId, userFilms));
        return userFilms;
    }

    public List<Film> getCommonFilms(int userId, int friendId) throws EntityNotFoundException {
        List<Film> result = new ArrayList<>();
        result.addAll(getUserFilms(userId));
        result.retainAll(getUserFilms(friendId));
        result.stream().sorted((film1, film2) -> film1.getLikes().size() - film2.getLikes().size())
                .collect(Collectors.toList());
        return result;
    }

    public List<Film> findPopularByDirector(int id, String sortBy) throws EntityNotFoundException {
        return filmStorage.getPopularByDirector(id, sortBy);
    }

    public void validateFilm(Film film) throws ValidationException, EntityNotFoundException {
        if (!StringUtils.hasText(film.getName())) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (StringUtils.hasText(film.getDescription()) && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        mpaDao.findMpaById(film.getMpa().getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreDao.findGenreById(genre.getId());
            }
        }
        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                directorService.findById(director.getId());
            }
        }
    }

    void checkExistFilmAndUser(int filmId, int userId) throws EntityNotFoundException {
        filmStorage.getFilmById(filmId).orElseThrow(() -> new EntityNotFoundException(
                String.format("Фильм с id=%d не существует", filmId)));
        userService.findById(userId);
    }
}
