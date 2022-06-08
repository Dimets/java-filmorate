package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private static int userId = 1;
    private final int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.id = userId++;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
