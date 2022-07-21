package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.UnknownDirectorException;
import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmDirectorDao {
    Set<Director> getFilmDirectors(int id) throws UnknownDirectorException;

    void setFilmDirectors(Set<Director> directors, Film film);

    void deleteFilmDirectors(Film film);
}
