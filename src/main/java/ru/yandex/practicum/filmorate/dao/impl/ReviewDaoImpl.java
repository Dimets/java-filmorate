package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.ReviewDao;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ReviewDaoImpl implements ReviewDao {

    private final JdbcTemplate jdbcTemplate;


    public ReviewDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review createReview(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO REVIEW (CONTENT, IS_POSITIVE, USER_ID, FILM_ID) VALUES(?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, review.getContent());
            ps.setBoolean(2, review.getIsPositive());
            ps.setInt(3, review.getUserId());
            ps.setInt(4, review.getFilmId());
            return ps;
        }, keyHolder);

        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sql = "UPDATE REVIEW SET CONTENT=?, IS_POSITIVE=? WHERE ID=?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
        return review;
    }

    @Override
    public void deleteReview(int id) {
        String sql = "DELETE FROM REVIEW WHERE ID=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public  Review getReviewById(long id) {
        String sql = "SELECT * FROM REVIEW WHERE id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeReview(rs), id);

    }

    @Override
    public List<Review> getReviewByIdFilm(int idFilm, int count) {
        String sql = "SELECT * FROM REVIEW WHERE film_id=?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs), idFilm)
                .stream()
                .sorted((a, b) -> b.getUseful() - a.getUseful())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(long idReview, int idUser) {
        deleteLike(idReview, idUser);
        String sql = "INSERT INTO REVIEW_LIKES (REVIEW_ID, USER_ID, IS_USEFUL)" +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, idReview, idUser, true);
    }

    @Override
    public void addDislike(long idReview, int idUser) {
        deleteLike(idReview, idUser);
        String sql = "INSERT INTO REVIEW_LIKES (REVIEW_ID, USER_ID, IS_USEFUL)" +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, idReview, idUser, false);
    }

    @Override
    public void deleteLike(long idReview, int idUser) {
        String sql = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID=? AND USER_ID=?";
        jdbcTemplate.update(sql, idReview, idUser);
    }

    @Override
    public void deleteDislike(long idReview, int idUser) {
        String sql = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID=? AND USER_ID=? AND IS_USEFUL=?";
        jdbcTemplate.update(sql, idReview, idUser, false);
    }

    @Override
    public List<Review> getAllReview(int count) {
        String sql = "SELECT id, content, is_positive, user_id, film_id FROM REVIEW";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeReview(rs))
                .stream()
                .sorted((a, b) -> b.getUseful() - a.getUseful())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Review makeReview(ResultSet rs) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(getUseful(rs.getInt("id")))
                .build();
    }

    private int getUseful(long idReview){
        String sqlCountLike = "SELECT COUNT(*) FROM REVIEW_LIKES WHERE IS_USEFUL=true AND REVIEW_ID=?";
        String sqlCountDislike = "SELECT COUNT(*) FROM REVIEW_LIKES WHERE IS_USEFUL=false AND REVIEW_ID=?";
        return jdbcTemplate.queryForObject(sqlCountLike, Integer.class, idReview) -
                jdbcTemplate.queryForObject(sqlCountDislike, Integer.class, idReview);
    }
}
