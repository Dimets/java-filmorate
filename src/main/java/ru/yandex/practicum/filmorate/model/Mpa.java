package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Mpa {
    private int id;
    private String name;

    public Mpa() {
    }

    public Mpa(String name) {
        this.name = name;
    }
}

