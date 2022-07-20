package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewDao {
    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(int id);

    Review getReviewById(long id);

    List<Review> getReviewByIdFilm(int idFilm, int count);

    void addLike(long idReview, int idUser);

    void addDislike(long idReview, int idUser);

    void deleteLike(long idReview, int idUser);

    void deleteDislike(long idReview, int idUser);

    List<Review> getAllReview(int count);
}
