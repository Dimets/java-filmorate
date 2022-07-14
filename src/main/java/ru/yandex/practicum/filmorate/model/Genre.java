package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre implements Comparable<Genre> {
    private int id;
    private String name;

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public int compareTo (Genre o) {
        if (this.getId() > o.getId()) {
            return 1;
        }
        if (this.getId() < o.getId()) {
            return -1;
        }
        return 0;
    }
}
