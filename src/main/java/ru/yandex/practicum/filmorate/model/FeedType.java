package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FeedType {
    private int id;
    private String type;

    public FeedType() {
    }

    public FeedType(String type) {
        this.type = type;
    }
}
