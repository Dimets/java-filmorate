package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FeedTypeDao;
import ru.yandex.practicum.filmorate.exception.UnknownFeedTypeException;
import ru.yandex.practicum.filmorate.model.FeedType;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FeedTypeService {
    private final FeedTypeDao feedTypeDao;

    public FeedTypeService(FeedTypeDao feedTypeDao) {
        this.feedTypeDao = feedTypeDao;
    }

    public Optional<FeedType> findById(int id) throws UnknownFeedTypeException {
        if (feedTypeDao.findFeedTypeById(id).isEmpty()) {
            throw new UnknownFeedTypeException(String.format("Тип события с id=%d не существует", id));
        }
        return feedTypeDao.findFeedTypeById(id);
    }

    public List<FeedType> findAll() {
        return feedTypeDao.getAllFeedType();
    }
}
