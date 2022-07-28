package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film) throws EntityNotFoundException;

    Film updateFilm(Film film) throws EntityNotFoundException;

    void deleteFilm(int id);

    List<Film> searchFilmByDirector(String query) throws EntityNotFoundException;

    List<Film> searchFilmByTitle(String query) throws EntityNotFoundException;

    Optional<Film> getFilmById(int id) throws EntityNotFoundException;

    List<Film> getAllFilms() throws EntityNotFoundException;

    List<Film> getPopular(int count) throws EntityNotFoundException;

    List<Film> getPopularByDirector(int id, String sortBy) throws EntityNotFoundException;

    List<Film> getPopular(Integer count, Optional<Integer> genreId, Optional<Integer> year)
            throws EntityNotFoundException;
}
