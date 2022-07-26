package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FeedOperationDao;
import ru.yandex.practicum.filmorate.exception.UnknownFeedOperationException;
import ru.yandex.practicum.filmorate.model.FeedOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FeedOperationDaoImpl implements FeedOperationDao {
    private final JdbcTemplate jdbcTemplate;

    public FeedOperationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<FeedOperation> findFeedOperationById(int id) throws UnknownFeedOperationException {
        SqlRowSet feedOperationRows = jdbcTemplate.queryForRowSet("select * from feed_operation where id = ?", id);
        if (feedOperationRows.next()) {
            FeedOperation feedOperation = new FeedOperation(feedOperationRows.getString("operation"));
            feedOperation.setId(id);
            return Optional.of(feedOperation);
        } else {
            throw new UnknownFeedOperationException(String.format(
                    "Операция для события ленты с id=%d не существует", id));
        }
    }

    @Override
    public List<FeedOperation> getAllFeedOperation() {
        SqlRowSet feedOperationRows = jdbcTemplate.queryForRowSet("select * from feed_operation order by operation");
        List<FeedOperation> feedOperationList = new ArrayList<>();

        while (feedOperationRows.next()) {
            FeedOperation feedOperation = new FeedOperation(
                    feedOperationRows.getString("operation")
            );
            feedOperation.setId(feedOperationRows.getInt("id"));
            feedOperationList.add(feedOperation);
        }
        return feedOperationList;
    }

    @Override
    public Optional<FeedOperation> getFeedOperationByOperation(String operation) throws UnknownFeedOperationException {
        SqlRowSet feedOperationRows = jdbcTemplate.queryForRowSet(
                "select * from feed_operation where operation = ?", operation);
        if (feedOperationRows.next()) {
            FeedOperation feedOperation = new FeedOperation(feedOperationRows.getString("operation"));
            feedOperation.setId(feedOperationRows.getInt("id"));
            return Optional.of(feedOperation);
        } else {
            throw new UnknownFeedOperationException(String.format(
                    "Операция %s для события ленты не существует", operation));
        }
    }
}
