package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.FeedType;

public interface FeedTypeDao {
    FeedType getFeedTypeByName(String type) throws EntityNotFoundException;
}
