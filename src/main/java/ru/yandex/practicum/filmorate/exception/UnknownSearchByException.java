package ru.yandex.practicum.filmorate.exception;

public class UnknownSearchByException extends RuntimeException{

    public UnknownSearchByException(String message) {
        super(message);
    }
}
