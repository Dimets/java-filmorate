package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "insert into friends (user_id, friend_id, status_id) values (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, 2);
        jdbcTemplate.update(sql, friendId, userId, 1);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public Set<Integer> getUserFriends(int id) {
        SqlRowSet userFriendsRows = jdbcTemplate.queryForRowSet("select friend_id from friends " +
                "where user_id = ? and status_id = ?", id, 2);
        Set<Integer> userFriendsSet = new HashSet<>();

        while (userFriendsRows.next()) {
            userFriendsSet.add(userFriendsRows.getInt("friend_id"));
        }
        return userFriendsSet;
    }

    @Override
    public void confirmFriend(int userId, int friendId) {
        String sql = "update friends set status_id = ? where user_id = ? and friend_id = ? and status_id = ?";
        jdbcTemplate.update(sql, 2, userId, friendId, 1);
    }
}
