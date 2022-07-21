package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {
    Optional<Genre> findGenreById(int id) throws UnknownGenreException;

    List<Genre> getAllGenre();

}
