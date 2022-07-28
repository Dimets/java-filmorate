package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MpaService  {
    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Optional<Mpa> findById(int id) throws EntityNotFoundException {
        if (mpaDao.findMpaById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Рейтинг MPA с id=%d не существует", id));
        }
        return mpaDao.findMpaById(id);
    }

    public List<Mpa> findAll() {
        return mpaDao.getAllMpa();
    }
}
