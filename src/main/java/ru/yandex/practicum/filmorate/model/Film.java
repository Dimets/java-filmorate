package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private static int filmId = 1;
    private int id;
    private String name;
    private String description;
    private Mpa mpa;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private Set<User> likes = new HashSet<>();
    private Set<Genre> genres;


    public Film(String name, String description, Mpa mpa, LocalDate releaseDate,
                int duration, int rate, Set<Genre> genres) {
        this.name = name;
        this.description = description;
        this.mpa = mpa;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genres = genres;
    }

    public static int getNextId() {
        return filmId++;
    }

}
