package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.exception.UnknownMpaException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final FilmGenreDao filmGenreDao;
    private final FilmLikeDao filmLikeDao;
    private final UserService userService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaService mpaService, FilmGenreDao filmGenreDao,
                         FilmLikeDao filmLikeDao, UserService userService) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaService = mpaService;
        this.filmGenreDao = filmGenreDao;
        this.filmLikeDao = filmLikeDao;
        this.userService = userService;
    }

    @Override
    public Film createFilm(Film film) throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        String sql = "insert into film (name, description, release_date, duration,rating_mpa_id, rate) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().intValue());
        if (film.getGenres() != null) {
            filmGenreDao.setFilmGenres(film.getGenres(), film);
        } else {
            film.setGenres(Collections.emptySet());
        }
        return getFilmById(keyHolder.getKey().intValue()).get();
    }

    @Override
    public Film updateFilm(Film film) throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        if (film.equals(getFilmById(film.getId()))) {
            log.info("Фильм id={} не изменился", film.getId());
        } else {
            String sqlQuery = "update film set name = ?, description = ?, release_date = ?, duration = ?, " +
                    "rating_mpa_id = ?, rate = ?  where id = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getRate()
                    , film.getId());
            if (film.getGenres() != null) {
                filmGenreDao.deleteFilmGenres(film);
                filmGenreDao.setFilmGenres(film.getGenres(), film);
            } else {
                filmGenreDao.deleteFilmGenres(film);
                film.setGenres(Collections.emptySet());
            }
        }
        return getFilmById(film.getId()).get();
    }

    @Override
    public void deleteFilm(int id) {
        String sqlQuery = "delete from film where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<Film> getFilmById(int id) throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", id);
        Set<User> userLikesSet = new HashSet<>();

        if (filmRows.next()) {
             Film film = new Film(
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    mpaService.findById(filmRows.getInt("rating_mpa_id")).get(),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getInt("duration"),
                    filmRows.getInt("rate"),
                    filmGenreDao.getFilmGenres(id)
            );

            film.setId(filmRows.getInt("id"));

            for (int i : filmLikeDao.getFilmLikes(film.getId())) {
                userLikesSet.add(userService.findById(i));
            }
            film.setLikes(userLikesSet);
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public List<Film> getAllFilms() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILM");

        List<Film> filmList = new ArrayList<>();

        while (filmRows.next()) {
            filmList.add(getFilmById(filmRows.getInt("id")).get());
        }
        return filmList;
    }

    @Override
    public List<Film> getPopular(int count) throws UnknownMpaException, UnknownGenreException, UnknownUserException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILM f LEFT JOIN " +
                "(SELECT film_id, count(user_id) likes_count FROM FILM_LIKE group by film_id) fl ON fl.film_id = f.id " +
                "ORDER BY likes_count DESC LIMIT ?", count);

        List<Film> filmList = new ArrayList<>();

        while (filmRows.next()) {
            filmList.add(getFilmById(filmRows.getInt("id")).get());
        }
        return filmList;
    }

    @Override
    public List<Film> getPopularByGenreAndYear(Integer count, Integer genreId, Integer year) {
        if (genreId == null) {
            return getPopularByYear(count, year);
        } else if (year == null) {
            return getPopularByGenre(count, genreId);
        }
        String sql = "SELECT f.id FROM film as f " +
                "       right join FILM_GENRE FG on f.id = FG.FILM_ID\n" +
                "        WHERE fg.GENRE_ID=?\n" +
                "        group by f.id\n" +
                "        having EXTRACT(YEAR from f.RELEASE_DATE) = ?\n" +
                "        limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                    try {
                        return getFilmById(rs.getInt("id")).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                },genreId, year, count);
    }
    private List<Film> getPopularByGenre(Integer count, Integer genreId) {
        String sql = "SELECT f.id FROM film as f " +
                "       right join FILM_GENRE FG on f.id = FG.FILM_ID\n" +
                "        WHERE fg.GENRE_ID=?\n" +
                "        limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                    try {
                        return getFilmById(rs.getInt("id")).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }, genreId, count);
    }

    private List<Film> getPopularByYear(Integer count, Integer year) {
        String sql = "SELECT f.id FROM film as f " +
                "        group by f.id\n" +
                "        having EXTRACT(YEAR from f.RELEASE_DATE) = ?\n" +
                "        limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
                    try {
                        return getFilmById(rs.getInt("id")).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }, year, count);
    }

}
