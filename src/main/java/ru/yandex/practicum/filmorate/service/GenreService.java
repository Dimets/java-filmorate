package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Optional<Genre> findById(int id) throws UnknownGenreException {
        if (genreDao.findGenreById(id).isEmpty()) {
            throw new UnknownGenreException(String.format("Жанр с id=%d не существует", id));
        }
        return genreDao.findGenreById(id);
    }

    public List<Genre> findAll() {
        return genreDao.getAllGenre();
    }
}
