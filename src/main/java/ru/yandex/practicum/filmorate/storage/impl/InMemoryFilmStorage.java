package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UnknownDirectorException;
import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.exception.UnknownMpaException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(Film.getNextId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        films.remove(id);
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        return Optional.of(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getPopular(int count) throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        List<Film> popularFilms = getAllFilms();
        Collections.sort(popularFilms, (o1, o2) -> o2.getLikes().size() - o1.getLikes().size());
        return popularFilms.subList(0, count > popularFilms.size() ? popularFilms.size() : count);
    }

    @Override
    public List<Film> getPopular(Integer count, Optional<Integer> genreId, Optional<Integer> year) {
        return null;
    }

    @Override
    public List<Film> getPopularByDirector(int id, String sortBy) throws UnknownMpaException, UnknownGenreException, UnknownUserException, UnknownDirectorException {
        List<Film> popularFilms = getAllFilms();
        List<Film> popularFilmsByDirector = new ArrayList<>();
        for (Film film: popularFilms) {
            Set<Director> directors = film.getDirectors();
            for (Director director: directors) {
                if (director.getId() == id) {
                    popularFilmsByDirector.add(film);
                }
            }
        }
        if (sortBy.equals("year")) {
            Collections.sort(popularFilmsByDirector, Comparator.comparing(Film::getReleaseDate));
        } else if ((sortBy.equals("likes"))) {
            Collections.sort(popularFilmsByDirector, (o1, o2) -> o2.getLikes().size() - o1.getLikes().size());
        }

        return popularFilmsByDirector;
    }
}
