package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FeedOperation {
    private int id;
    private  String operation;

    public FeedOperation() {
    }

    public FeedOperation(String operation) {
        this.operation = operation;
    }
}
