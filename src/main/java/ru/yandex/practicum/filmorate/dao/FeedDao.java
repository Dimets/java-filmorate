package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.UnknownFeedOperationException;
import ru.yandex.practicum.filmorate.exception.UnknownFeedTypeException;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;
import java.util.Optional;

public interface FeedDao {
    Feed createFeed(Feed feed);

    List<Feed> getFeedByUser(int userId) throws UnknownFeedTypeException, UnknownFeedOperationException;

    Optional<Feed> getFeedById(int id) throws UnknownFeedOperationException, UnknownFeedTypeException;
}
