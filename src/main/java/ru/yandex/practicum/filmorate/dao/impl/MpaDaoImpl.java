package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> findMpaById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from rating_mpa where id = ?", id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(mpaRows.getString("rating_name"));
            mpa.setId(id);
            return Optional.of(mpa);
        } else {
            log.info("Рейтинг MPA c id={} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from rating_mpa");
        List<Mpa> mpaList = new ArrayList<>();

        while (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getString("rating_name")
            );
            mpa.setId(mpaRows.getInt("id"));
            mpaList.add(mpa);
        }
        return mpaList;
    }
}
