package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class FilmLikeDaoImpl implements FilmLikeDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Integer> getFilmLikes(int id) {
        SqlRowSet filmLikeRows = jdbcTemplate.queryForRowSet("select * from film_like where film_id = ?", id);
        Set<Integer> filmLikeSet = new HashSet<>();

        while (filmLikeRows.next()) {
            filmLikeSet.add(filmLikeRows.getInt("user_id"));
        }
        return filmLikeSet;
    }
    @Override
    public Set<Integer> getUserLikes(int userId) {
        SqlRowSet userFilmsRows = jdbcTemplate.queryForRowSet("select film_id from film_like " +
                "where user_id = ?", userId);
        Set<Integer> userFilmsSet = new HashSet<>();

        while (userFilmsRows.next()) {
            userFilmsSet.add(userFilmsRows.getInt("film_id"));
        }
        return userFilmsSet;
    }

    @Override
    public void addFilmLike(int filmId, int userId) {
        String sql = "insert into film_like (film_id, user_id) values (?, ?)";
        log.info("sql={}", sql);
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteFilmLike(int filmId, int userId) {
        String sql = "delete from film_like where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
