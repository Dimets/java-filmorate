package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;


    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review createReview(Review review) {
        return null;
    }

    @Override
    public Review updateReview(Review review) {
        return null;
    }

    @Override
    public void deleteReview(int id) {

    }

    @Override
    public Optional<Review> getReviewById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Review> getReviewByIdFilm(int idFilm, int count) {
        return null;
    }

    @Override
    public void addLike(long idReview, int idUser) {

    }

    @Override
    public void addDislike(long idReview, int idUser) {

    }

    @Override
    public void deleteLike(long idReview, int idUser) {

    }

    @Override
    public void deleteDislike(long idReview, int idUser) {

    }
}
