package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Director implements Comparable<Director> {
    private int id;
    private String name;

    public Director() {
    }

    public Director(String name) {
        this.name = name;
    }

    public Director(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo (Director d) {
        if (this.getId() > d.getId()) {
            return 1;
        }
        if (this.getId() < d.getId()) {
            return -1;
        }
        return 0;
    }
}
