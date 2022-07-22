package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        log.info("POST /users {}", user);
        return userService.create(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) throws ValidationException, UnknownUserException {
        log.info("PUT /users {}", user);
        return userService.update(user);
    }
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("userId") int userid) throws UnknownUserException {
        log.info("DELETE /users/" + userid);
        userService.deleteById(userid);
    }

    @GetMapping
    public List<User> findAll() {
        log.info("GET /users");
        return userService.findAll();
    }
    @GetMapping("/{userId}")
    public User findUser(@PathVariable("userId") int userId) throws UnknownUserException {
        log.info("GET /users/" + userId);
        return userService.findById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId)
            throws UnknownUserException, FriendException {
        log.info("PUT /friends");
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("{userId}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable("userId") int userId, @PathVariable("friendId") int friendId)
            throws UnknownUserException, FriendException {
        log.info("DELETE /friends");
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("{userId}/friends")
    public List<User> findFriends(@PathVariable("userId") int userId) throws UnknownUserException {
        log.info("GET /friends");
        return userService.getUserFriends(userId);
    }

    @GetMapping("{userId}/friends/common/{otherUserId}")
    public List<User> findCommonFriends(@PathVariable("userId") int userId,
                                        @PathVariable("otherUserId") int otherUserId) throws UnknownUserException {
        log.info("GET /common friends");
        return userService.getCommonFriends(userId, otherUserId);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable int id) throws UnknownMpaException, UnknownGenreException,
            UnknownUserException, UnknownDirectorException, UnknownFilmException {
        return userService.getRecommendations(id);
    }
}
