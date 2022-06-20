package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    void deleteUser(int id);

    User getUserById(int id);

    List<User> getAllUsers();
}
