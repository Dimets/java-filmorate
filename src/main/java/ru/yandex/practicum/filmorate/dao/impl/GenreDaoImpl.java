package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findGenreById(int id) throws EntityNotFoundException {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre(genreRows.getString("genre_name"));
            genre.setId(id);
            return Optional.of(genre);
        } else {
            throw new EntityNotFoundException(String.format("Жанра с id=%d не существует", id));
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre order by id");
        List<Genre> genreList = new ArrayList<>();

        while (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getString("genre_name")
            );
            genre.setId(genreRows.getInt("id"));
            genreList.add(genre);
        }
        return genreList;
    }
}
