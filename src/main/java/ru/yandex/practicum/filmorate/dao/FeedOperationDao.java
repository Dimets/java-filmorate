package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.UnknownFeedOperationException;
import ru.yandex.practicum.filmorate.model.FeedOperation;

import java.util.List;
import java.util.Optional;

public interface FeedOperationDao {
    Optional<FeedOperation> findFeedOperationById(int id) throws UnknownFeedOperationException;

    List<FeedOperation> getAllFeedOperation();

    Optional<FeedOperation> getFeedOperationByOperation(String operation) throws UnknownFeedOperationException;
}
