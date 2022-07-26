package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedDao {
    Feed createFeed(Feed feed);

    List<Feed> getFeedByUser(int userId);
}
