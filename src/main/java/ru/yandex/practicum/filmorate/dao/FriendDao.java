package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FriendDao {
    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    Set<Integer> getUserFriends(int id);

    void confirmFriend(int userId, int friendId);
}
