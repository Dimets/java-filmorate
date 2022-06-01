package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("POST /users {}", user);

        /*if (user.getLogin().isEmpty() || user.getLogin().length() == 0 || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробел");
        }
        if (user.getEmail().isEmpty() || user.getEmail().length() == 0 || !user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать @ и не быть пустым");
        }
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() !=null && user.getBirthday().isAfter(LocalDate.now())) {
           throw new ValidationException("Дата рождения не может быть в будущем");
        }
        */

        if (validate(user)) {
            users.put(user.getId(), user);
            log.info("Пользователь создан: {}", user);
        }
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("PUT /users {}", user);
       if (users.containsKey(user.getId())) {
           if (validate(user)) {
               users.put(user.getId(), user);
           }
       } else {
            throw new UnknownUserException("Пользователя с таким id не существует!");
       }
        log.info("Пользователь изменен: {}", user);
        return user;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("GET /users");
        return new ArrayList<>(users.values());
    }

    public static boolean validate(User user) {
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробел");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email должен содержать @ и не быть пустым");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() !=null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }

}
