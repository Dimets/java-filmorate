package ru.yandex.practicum.filmorate.exception;

public class UnknownFeedTypeException extends RuntimeException{
    public UnknownFeedTypeException(String message) {
        super(message);
    }
}
