package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FeedTypeDao;
import ru.yandex.practicum.filmorate.exception.UnknownFeedTypeException;
import ru.yandex.practicum.filmorate.model.FeedType;

import java.util.Optional;

@Component
@Slf4j
public class FeedTypeDaoImpl implements FeedTypeDao {
    private final JdbcTemplate jdbcTemplate;

    public FeedTypeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public FeedType getFeedTypeByName(String type) throws UnknownFeedTypeException {
        SqlRowSet feedTypeRow = jdbcTemplate.queryForRowSet("SELECT * FROM FEED_TYPE WHERE TYPE = ?", type);
        if (feedTypeRow.next()) {
            FeedType feedType = new FeedType(feedTypeRow.getString("type"));
            feedType.setId(feedTypeRow.getInt("id"));
            return feedType;
        } else {
            throw new UnknownFeedTypeException(String.format("Тип события %s не найден", type));
        }
    }
}
