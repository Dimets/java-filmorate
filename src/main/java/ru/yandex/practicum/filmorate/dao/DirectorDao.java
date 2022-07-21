package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorDao {
    Director createDirector(Director director);
    Director updateDirector(Director director);
    void deleteDirector(int id);
    Optional<Director> getDirectorById(int id);
    List<Director> getAllDirectors();
}