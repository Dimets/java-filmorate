package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.FriendException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j

public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage){
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User create(User user) throws ValidationException {
        validateUser(user);
        user.setId(User.getNextId());
        inMemoryUserStorage.createUser(user);
        log.info("Пользователь создан: {}", user.getId());
        log.debug("Пользователь создан: {}", user);
        return user;
    }

    public User update(User user) throws ValidationException, UnknownUserException {
        validateUser(user);
        if (inMemoryUserStorage.getUserById(user.getId()) != null) {
            inMemoryUserStorage.updateUser(user);
            log.info("Пользователь изменен: {}", user.getId());
            log.debug("Пользователь изменен: {}", user);
            return user;
        } else {
            throw new UnknownUserException(String.format("Пользователь с id=%d не существует", user.getId()));
        }
    }

    public void deleteById(int id) throws UnknownUserException {
        if (inMemoryUserStorage.getUserById(id) != null) {
            log.info(String.format("Пользователь с id=%d удален:", id) + inMemoryUserStorage.getUserById(id));
            inMemoryUserStorage.deleteUser(id);
        } else {
            throw new UnknownUserException(String.format("Пользователь с id=%d не существует", id));
        }
    }

    public List<User> findAll() {
        log.info("Список пользователей получен: " + inMemoryUserStorage.getAllUsers());
        return inMemoryUserStorage.getAllUsers();
    }

    public User findById(int id) throws UnknownUserException {
        if (inMemoryUserStorage.getUserById(id) != null) {
            log.info(String.format("Пользователь с id=%d найден:", id) + inMemoryUserStorage.getUserById(id));
            return inMemoryUserStorage.getUserById(id);
        } else {
           throw new UnknownUserException(String.format("Пользователь с id=%d не существует", id));
        }
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
        findById(userId).getFriends().add(friendId);
        log.info(String.format("Пользователь с id=%d добавлен в друзья к пользователю с id=%d:", friendId, userId));
        findById(friendId).getFriends().add(userId);
        log.info(String.format("Пользователь с id=%d добавлен в друзья к пользователю с id=%d:", userId, friendId));
    }

    public void deleteFriend(int userId, int friendId) throws UnknownUserException, FriendException {
        checkExistUserAndFriend(userId, friendId);
        if (findById(userId).getFriends().contains(friendId)) {
            findById(userId).getFriends().remove(friendId);
            log.info(String.format("Пользователь с id=%d удален из друзей пользователя с id=%d:", friendId, userId));
            findById(friendId).getFriends().remove(userId);
            log.info(String.format("Пользователь с id=%d удален из друзей пользователя с id=%d:", userId,friendId));
        } else {
            throw new FriendException((String.format("Пользователя с id=%d нет в списке друзей пользователя" +
                    " с id=%d", friendId, userId)));
        }
    }

    public List<User> getUserFriends(int userId) throws UnknownUserException {
        List<User> userFriends = new ArrayList<>();
        for (int i : findById(userId).getFriends()) {
            userFriends.add(findById(i));
        }
        log.info(String.format("Список друзей пользователя id=%d: {}", userId, userFriends));
        return userFriends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) throws UnknownUserException {
        List<User> result = new ArrayList<>();
        result.addAll(getUserFriends(userId));
        result.retainAll(getUserFriends(otherUserId));
        return result;
    }

    public void checkExistUserAndFriend(int userId, int friendId) throws UnknownUserException {
        if (inMemoryUserStorage.getUserById(userId) == null) {
            throw new UnknownUserException(String.format("Пользователь с id=%d не существует", userId));
        }
        if (inMemoryUserStorage.getUserById(friendId) == null) {
            throw new UnknownUserException((String.format("Пользователь с id=%d не существует", friendId)));
        }
    }

}
