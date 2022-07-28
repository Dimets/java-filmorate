package ru.yandex.practicum.filmorate.dao;

import java.util.Set;

public interface FilmLikeDao {
    Set<Integer> getFilmLikes(int id);

    void addFilmLike(int filmId, int userId);

    void deleteFilmLike(int filmId, int userId);
    Set<Integer> getUserLikes(int userId);
}
