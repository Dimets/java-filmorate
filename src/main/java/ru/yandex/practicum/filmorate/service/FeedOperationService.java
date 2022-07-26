package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FeedOperationDao;
import ru.yandex.practicum.filmorate.exception.UnknownFeedOperationException;
import ru.yandex.practicum.filmorate.model.FeedOperation;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FeedOperationService {
    private FeedOperationDao feedOperationDao;

    public FeedOperationService(FeedOperationDao feedOperationDao) {
        this.feedOperationDao = feedOperationDao;
    }

    public Optional<FeedOperation> findById(int id) throws UnknownFeedOperationException {
        if (feedOperationDao.findFeedOperationById(id).isEmpty()) {
            throw new UnknownFeedOperationException(String.format("Тип события с id=%d не существует", id));
        }
        return feedOperationDao.findFeedOperationById(id);
    }

    public List<FeedOperation> findAll() {
        return feedOperationDao.getAllFeedOperation();
    }
}
