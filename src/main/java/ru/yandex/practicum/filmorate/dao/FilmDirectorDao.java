package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;

public interface FilmDirectorDao {
    Set<Director> getFilmDirectors(int id) throws EntityNotFoundException;

    void setFilmDirectors(Set<Director> directors, Film film);

    void deleteFilmDirectors(Film film);
}
