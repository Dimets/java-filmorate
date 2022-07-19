package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    ReviewDao reviewDao;


    public Review createReview(Review review) {
        return reviewDao.createReview(review);
    }


    public Review updateReview(Review review) {
        return reviewDao.updateReview(review);
    }


    public void deleteReview(int id) {
        reviewDao.deleteReview(id);
    }


    public Review getReviewById(int id) {
        return reviewDao.getReviewById(id).get();
    }


    public List<Review> getReviewByIdFilm(int idFilm, int count) {
        return reviewDao.getReviewByIdFilm(idFilm, count);
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

}
