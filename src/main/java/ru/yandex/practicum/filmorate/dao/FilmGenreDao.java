package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmGenreDao {
    Set<Genre> getFilmGenres(int id) throws EntityNotFoundException;

    void setFilmGenres(Set<Genre> genres, Film film);

    void deleteFilmGenres(Film film);
}
