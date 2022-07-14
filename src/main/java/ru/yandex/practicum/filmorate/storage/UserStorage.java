package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    Optional<User> updateUser(User user);

    void deleteUser(int id);

    Optional<User> getUserById(int id);

    List<User> getAllUsers();
}
