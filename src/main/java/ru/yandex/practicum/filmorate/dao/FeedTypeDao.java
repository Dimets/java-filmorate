package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.UnknownFeedTypeException;
import ru.yandex.practicum.filmorate.model.FeedType;

import java.util.List;
import java.util.Optional;

public interface FeedTypeDao {
    Optional<FeedType> findFeedTypeById(int id) throws UnknownFeedTypeException;

    List<FeedType> getAllFeedType();

    Optional<FeedType> getFeedTypeByType(String type) throws UnknownFeedTypeException;
}
