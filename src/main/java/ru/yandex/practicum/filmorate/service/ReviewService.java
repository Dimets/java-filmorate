package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

@Service
@Slf4j
public class ReviewService {

    private final ReviewDao reviewDao;

    @Autowired
    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }


    public Review createReview(Review review) throws ValidationException, UnknownFilmException, UnknownUserException {
        validationReview(review);
        return reviewDao.createReview(review);
    }


    public Review updateReview(Review review) throws ValidationException, UnknownFilmException, UnknownUserException {
        validationReview(review);
        reviewDao.updateReview(review);
        return getReviewById(review.getReviewId());
    }


    public void deleteReview(int id) {
        reviewDao.deleteReview(id);
    }


    public Review getReviewById(long id) {
        try {
            return reviewDao.getReviewById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UnknownReviewException(String.format("idReview=%d is missing in the database", id));
        }
    }

    public void addLike(long idReview, int idUser) {
        reviewDao.addLike(idReview, idUser);
    }


    public void addDislike(long idReview, int idUser) {
        reviewDao.addDislike(idReview, idUser);
    }


    public void deleteLike(long idReview, int idUser) {
        reviewDao.deleteLike(idReview, idUser);
    }


    public void deleteDislike(long idReview, int idUser) {
        reviewDao.deleteDislike(idReview, idUser);
    }

    public List<Review> getReview(Integer filmId, Integer count) {
        if (filmId == null && count == null)
            return reviewDao.getAllReview(10);

        if (count == null)
            return reviewDao.getReviewByIdFilm(filmId, 10);

        if (filmId == null)
            return reviewDao.getAllReview(count);

        return reviewDao.getReviewByIdFilm(filmId, count);
    }

    private void validationReview(Review review) throws ValidationException, UnknownUserException, UnknownFilmException {
        if (review.getFilmId() == 0)
            throw new ValidationException("not set filmId");

        if (review.getUserId() == 0)
            throw new ValidationException("not set userId");

        if (review.getIsPositive() == null)
            throw new ValidationException("not set isPositive");

        if (review.getUserId() < 0)
            throw new UnknownUserException(String.format("userId=%d is incorrect", review.getUserId()));

        if (review.getFilmId() < 0)
            throw new UnknownFilmException(String.format("filmId=%d is incorrect", review.getFilmId()));

    }
}
