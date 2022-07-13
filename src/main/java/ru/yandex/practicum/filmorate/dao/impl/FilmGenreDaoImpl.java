package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Set;
import java.util.TreeSet;

@Component
@Slf4j
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public Set<Genre> getFilmGenres(int id) throws UnknownGenreException {
        SqlRowSet filmGenreRows = jdbcTemplate.queryForRowSet("select * from film_genre where film_id = ?", id);
        Set<Genre> filmGenreSet = new TreeSet<>();

        while (filmGenreRows.next()) {
            filmGenreSet.add(genreService.findById(filmGenreRows.getInt("genre_id")).get());
        }
        return filmGenreSet;
    }

    @Override
    public void setFilmGenres(Set<Genre> genres, Film film) {
        String sql = "insert into film_genre (film_id, genre_id) values (?, ?)";

        for (Genre genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    @Override
    public void deleteFilmGenres(Film film) {
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }
}
