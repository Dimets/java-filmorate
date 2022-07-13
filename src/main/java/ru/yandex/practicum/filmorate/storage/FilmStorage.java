package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.exception.UnknownMpaException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film) throws UnknownMpaException, UnknownGenreException, UnknownUserException;

    Film updateFilm(Film film) throws UnknownMpaException, UnknownGenreException, UnknownUserException;

    void deleteFilm(int id);

    Film getFilmById(int id) throws UnknownMpaException, UnknownGenreException, UnknownUserException;

    List<Film> getAllFilms() throws UnknownMpaException, UnknownGenreException, UnknownUserException;
}
