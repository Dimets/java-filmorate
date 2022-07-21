package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDirectorDao;
import ru.yandex.practicum.filmorate.exception.UnknownDirectorException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.Set;
import java.util.TreeSet;

@Component
@Slf4j
public class FilmDirectorDaoImpl implements FilmDirectorDao {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorService directorService;

    public FilmDirectorDaoImpl(JdbcTemplate jdbcTemplate, DirectorService directorService) {
        this.jdbcTemplate = jdbcTemplate;
        this.directorService = directorService;
    }

    @Override
    public Set<Director> getFilmDirectors(int id) throws UnknownDirectorException {
        SqlRowSet filmDirectorsRows = jdbcTemplate.queryForRowSet("select * from film_director where film_id = ?", id);
        Set<Director> filmDirectorSet = new TreeSet<>();

        while (filmDirectorsRows.next()) {
            filmDirectorSet.add(directorService.findById(filmDirectorsRows.getInt("director_id")));
        }
        return filmDirectorSet;
    }

    @Override
    public void setFilmDirectors(Set<Director> directors, Film film) {
        String sql = "insert into film_director (film_id, director_id) values (?, ?)";

        for (Director director : directors) {
            jdbcTemplate.update(sql, film.getId(), director.getId());
        }
    }

    @Override
    public void deleteFilmDirectors(Film film) {
        String sqlQuery = "delete from film_director where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }
}
