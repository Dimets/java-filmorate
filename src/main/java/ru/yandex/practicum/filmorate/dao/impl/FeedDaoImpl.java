package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FeedDao;
import ru.yandex.practicum.filmorate.dao.FeedOperationDao;
import ru.yandex.practicum.filmorate.dao.FeedTypeDao;
import ru.yandex.practicum.filmorate.exception.UnknownFeedOperationException;
import ru.yandex.practicum.filmorate.exception.UnknownFeedTypeException;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class FeedDaoImpl implements FeedDao {
    private final JdbcTemplate jdbcTemplate;
    private final FeedOperationDao feedOperationDao;
    private final FeedTypeDao feedTypeDao;

    public FeedDaoImpl(JdbcTemplate jdbcTemplate, FeedOperationDao feedOperationDao, FeedTypeDao feedTypeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.feedOperationDao = feedOperationDao;
        this.feedTypeDao = feedTypeDao;
    }

    @Override
    public Feed createFeed(Feed feed) {
        String sql = "insert into user_feed (create_dttm, type_id, operation_id, entity_id,user_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setLong(1, feed.getTimestamp());
            stmt.setInt(2, feedTypeDao.getFeedTypeByType(feed.getEventType()).get().getId());
            stmt.setInt(3, feedOperationDao.getFeedOperationByOperation(
                        feed.getOperation()).get().getId());
            stmt.setLong(4, feed.getEntityId());
            stmt.setInt(5, feed.getUserId());
            return stmt;
        }, keyHolder);

        feed.setEventId(keyHolder.getKey().intValue());
        log.debug("Feed {} создан entityId={} userId={} EventType={} operation={}",
                feed.getEventId(),feed.getEntityId(),
                feed.getUserId(), feed.getEventType(), feed.getOperation());
        return feed;
    }

    @Override
    public List<Feed> getFeedByUser(int userId) throws UnknownFeedTypeException, UnknownFeedOperationException {
        SqlRowSet feedRows = jdbcTemplate.queryForRowSet("SELECT * FROM USER_FEED WHERE USER_ID = ?", userId);

        List<Feed> feedList = new ArrayList<>();

        while (feedRows.next()) {
            feedList.add(getFeedById(feedRows.getInt("id")).get());
        }
        return feedList;
    }

    @Override
    public Optional<Feed> getFeedById(int id) throws UnknownFeedOperationException, UnknownFeedTypeException {
        SqlRowSet feedRows = jdbcTemplate.queryForRowSet("select * from user_feed where id = ?", id);

        if (feedRows.next()) {
            Feed feed = new Feed(
                    feedTypeDao.findFeedTypeById(feedRows.getInt("type_id")).get().getType(),
                    feedOperationDao.findFeedOperationById(
                            feedRows.getInt("operation_id")).get().getOperation(),
                    feedRows.getLong("entity_id"),
                    feedRows.getInt("user_id"),
                    feedRows.getLong("create_dttm")
            );
            feed.setEventId(feedRows.getInt("id"));
            return Optional.of(feed);
        }
        return Optional.empty();
    }
}
