package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FeedOperationDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.FeedOperation;

@Component
@Slf4j
public class FeedOperationDaoImpl implements FeedOperationDao {
    private final JdbcTemplate jdbcTemplate;

    public FeedOperationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FeedOperation getFeedOperationByOperationName(String operation) throws EntityNotFoundException {
        SqlRowSet feedOperationRows = jdbcTemplate.queryForRowSet(
                "select * from feed_operation where operation = ?", operation);
        if (feedOperationRows.next()) {
            FeedOperation feedOperation = new FeedOperation(feedOperationRows.getString("operation"));
            feedOperation.setId(feedOperationRows.getInt("id"));
            return feedOperation;
        } else {
            throw new EntityNotFoundException(String.format(
                    "Операция %s для события ленты не существует", operation));
        }
    }
}
