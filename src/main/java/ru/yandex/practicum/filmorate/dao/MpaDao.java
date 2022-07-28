package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {
    Optional<Mpa> findMpaById(int id) throws EntityNotFoundException;

    List<Mpa> getAllMpa();
}
