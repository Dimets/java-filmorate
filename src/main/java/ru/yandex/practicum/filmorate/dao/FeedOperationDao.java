package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.UnknownFeedOperationException;
import ru.yandex.practicum.filmorate.model.FeedOperation;

import java.util.Optional;

public interface FeedOperationDao {
    Optional<FeedOperation> getFeedOperationByName(String operation) throws UnknownFeedOperationException;
}
