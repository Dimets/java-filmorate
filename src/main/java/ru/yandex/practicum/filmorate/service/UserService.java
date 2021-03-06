package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exception.FriendException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;
    @Autowired
    private  FriendDao friendDao;

    public User create(User user) throws ValidationException {
        validateUser(user);
        userStorage.createUser(user);
        log.info("Пользователь создан: {}", user.getId());
        log.debug("Пользователь создан: {}", user);
        return user;
    }

    public User update(User user) throws ValidationException, UnknownUserException {
        validateUser(user);
        userStorage.getUserById(user.getId()).orElseThrow(() -> new UnknownUserException(
                String.format("Пользователь с id=%d не существует", user.getId())));

        return userStorage.updateUser(user).get();
    }

    public void deleteById(int id) throws UnknownUserException {
        if (userStorage.getUserById(id) != null) {
            log.info(String.format("Пользователь с id=%d удален:", id) + userStorage.getUserById(id));
            userStorage.deleteUser(id);
        } else {
            throw new UnknownUserException(String.format("Пользователь с id=%d не существует", id));
        }
    }

    public List<User> findAll() {
        log.info("Список пользователей получен: " + userStorage.getAllUsers());
        return userStorage.getAllUsers();
    }

    public User findById(int id) throws UnknownUserException {
        return userStorage.getUserById(id).orElseThrow(() -> new UnknownUserException(
                String.format("Пользователь с id=%d не существует", id)));
    }

    public void validateUser(User user) throws ValidationException {
        if (!StringUtils.hasText(user.getLogin()) || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробел");
        }
        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать @ и не быть пустым");
        }
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() !=null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }

    public void addFriend(int userId, int friendId) throws UnknownUserException, FriendException {
        checkExistUserAndFriend(userId, friendId);
        if (getUserFriends(userId).contains(friendId)){
            throw new FriendException((String.format("Пользователь с id=%d уже есть в списке друзей пользователя" +
                            " с id=%d", friendId, userId)));
        }
        friendDao.addFriend(userId, friendId);
        log.info(String.format("Пользователь с id=%d добавлен в друзья к пользователю с id=%d:", friendId, userId));
    }

    public void deleteFriend(int userId, int friendId) throws UnknownUserException, FriendException {
        checkExistUserAndFriend(userId, friendId);
        if (friendDao.getUserFriends(userId).contains(friendId)) {
            friendDao.deleteFriend(userId, friendId);
            log.info(String.format("Пользователь с id=%d удален из друзей пользователя с id=%d:", friendId, userId));
            friendDao.deleteFriend(friendId, userId);
            log.info(String.format("Пользователь с id=%d удален из друзей пользователя с id=%d:", userId,friendId));
        } else {
            throw new FriendException((String.format("Пользователя с id=%d нет в списке друзей пользователя" +
                    " с id=%d", friendId, userId)));
        }
    }

    public List<User> getUserFriends(int userId) throws UnknownUserException {
        List<User> userFriends = new ArrayList<>();
        for (int i : friendDao.getUserFriends(userId)) {
            userFriends.add(findById(i));
        }
        log.debug(String.format("Список друзей пользователя id=%d: {}", userId, userFriends));
        return userFriends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) throws UnknownUserException {
        List<User> result = new ArrayList<>();
        result.addAll(getUserFriends(userId));
        result.retainAll(getUserFriends(otherUserId));
        return result;
    }

    public void checkExistUserAndFriend(int userId, int friendId) throws UnknownUserException {
        userStorage.getUserById(userId).orElseThrow(() -> new UnknownUserException(
                String.format("Пользователь с id=%d не существует", userId)));

        userStorage.getUserById(friendId).orElseThrow(() -> new UnknownUserException(
                String.format("Пользователь с id=%d не существует", friendId)));
/*
        if (userStorage.getUserById(userId) == null) {
            throw new UnknownUserException(String.format("Пользователь с id=%d не существует", userId));
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new UnknownUserException((String.format("Пользователь с id=%d не существует", friendId)));
        }*/
    }

}
