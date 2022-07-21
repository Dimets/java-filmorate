package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {
    Director createDirector(Director director);
    Director updateDirector(Director director);
    void deleteDirector(int id);
    Optional<Director> getDirectorById(int id);
    List<Director> getAllDirectors();
}
