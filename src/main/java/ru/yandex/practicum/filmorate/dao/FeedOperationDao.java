package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.FeedOperation;

public interface FeedOperationDao {
    FeedOperation getFeedOperationByOperationName(String operation) throws EntityNotFoundException;
}
