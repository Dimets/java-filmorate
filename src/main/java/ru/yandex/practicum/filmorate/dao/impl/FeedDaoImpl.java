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
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

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
            stmt.setInt(2, feedTypeDao.getFeedTypeByName(feed.getEventType()).get().getId());
            stmt.setInt(3, feedOperationDao.getFeedOperationByName(
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
    public List<Feed> getFeedByUser(int userId) {
        SqlRowSet feedRows = jdbcTemplate.queryForRowSet("SELECT uf.*, fo.OPERATION, ft.\"TYPE\" \n" +
                "FROM USER_FEED uf, FEED_OPERATION fo, FEED_TYPE ft \n" +
                "WHERE uf.USER_ID = ? AND fo.ID = uf.OPERATION_ID \n" +
                "AND ft.ID = uf.TYPE_ID ORDER BY uf.CREATE_DTTM ASC", userId);

        List<Feed> feedList = new ArrayList<>();

        while (feedRows.next()) {
            Feed feed = new Feed(
                    feedRows.getString("type"),
                    feedRows.getString("operation"),
                    feedRows.getLong("entity_id"),
                    feedRows.getInt("user_id"),
                    feedRows.getLong("create_dttm"));
            feed.setEventId(feedRows.getInt("id"));
            feedList.add(feed);
        }
        return feedList;
    }
}
