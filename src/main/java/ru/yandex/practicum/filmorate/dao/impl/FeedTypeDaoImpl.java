package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FeedTypeDao;
import ru.yandex.practicum.filmorate.exception.UnknownFeedTypeException;
import ru.yandex.practicum.filmorate.model.FeedType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FeedTypeDaoImpl implements FeedTypeDao {
    private final JdbcTemplate jdbcTemplate;

    public FeedTypeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<FeedType> findFeedTypeById(int id) throws UnknownFeedTypeException {
        SqlRowSet feedTypeRows = jdbcTemplate.queryForRowSet("select * from feed_type where id = ?", id);
        if (feedTypeRows.next()) {
            FeedType feedType = new FeedType(feedTypeRows.getString("type"));
            feedType.setId(id);
            return Optional.of(feedType);
        } else {
            throw new UnknownFeedTypeException(String.format("Тип события с id=%d не существует", id));
        }
    }

    @Override
    public List<FeedType> getAllFeedType() {
        SqlRowSet feedTypeRows = jdbcTemplate.queryForRowSet("select * from feed_type order by type");
        List<FeedType> feedTypeList = new ArrayList<>();

        while (feedTypeRows.next()) {
            FeedType feedType = new FeedType(
                    feedTypeRows.getString("type")
            );
            feedType.setId(feedTypeRows.getInt("id"));
            feedTypeList.add(feedType);
        }
        return feedTypeList;
    }

    @Override
    public Optional<FeedType> getFeedTypeByType(String type) throws UnknownFeedTypeException {
        SqlRowSet feedTypeRow = jdbcTemplate.queryForRowSet("SELECT * FROM FEED_TYPE WHERE TYPE = ?", type);
        if (feedTypeRow.next()) {
            FeedType feedType = new FeedType(feedTypeRow.getString("type"));
            feedType.setId(feedTypeRow.getInt("id"));
            return Optional.of(feedType);
        } else {
            throw new UnknownFeedTypeException(String.format("Тип события %s не найден", type));
        }
    }
}
