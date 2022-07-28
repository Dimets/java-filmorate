package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDao;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("directorDaoImpl")
@Slf4j
public class DirectorDaoImpl implements DirectorDao {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director createDirector(Director director) {
        String sql = "insert into director (director_name) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        return getDirectorById(director.getId()).get();
    }

    @Override
    public Director updateDirector(Director director) {
        if (director.equals(getDirectorById(director.getId()))) {
            log.info("Режиссер id={} не изменился", director.getId());
        } else {
            String sqlQuery = "update director set director_name = ?  where id = ?";
            jdbcTemplate.update(sqlQuery
                    , director.getName()
                    , director.getId());
        }
        return getDirectorById(director.getId()).get();
    }

    @Override
    public void deleteDirector(int id) {
        String sqlQuery = "delete from director where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<Director> getDirectorById(int id) {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("select * from director where id = ?", id);

        if (directorRows.next()) {
            Director director = new Director(directorRows.getInt("id"),
                    directorRows.getString("director_name")
            );
            return Optional.of(director);
        }
        return Optional.empty();
    }

    @Override
    public List<Director> getAllDirectors() {
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet("select * from director");
        List<Director> directorList = new ArrayList<>();

        while (directorRows.next()) {
            Director director = new Director(
                    directorRows.getString("director_name")
            );
            director.setId(directorRows.getInt("id"));
            directorList.add(director);
        }
        return directorList;
    }
}
