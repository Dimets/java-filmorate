package ru.yandex.practicum.filmorate.exception;

public class UnknownFeedOperationException extends RuntimeException{
    public UnknownFeedOperationException(String message) {
        super(message);
    }
}
