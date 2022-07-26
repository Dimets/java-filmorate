package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.UnknownFeedTypeException;
import ru.yandex.practicum.filmorate.model.FeedType;

import java.util.Optional;

public interface FeedTypeDao {
    FeedType getFeedTypeByName(String type) throws UnknownFeedTypeException;
}
