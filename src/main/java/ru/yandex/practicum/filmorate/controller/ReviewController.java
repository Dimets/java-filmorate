package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(required = false) Integer filmId,
                                   @RequestParam(required = false) Integer count) {
        log.info("GET /reviews filmId={} count={}", filmId, count);
        return reviewService.getReview(filmId, count);
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) throws ValidationException {
        log.info("POST /reviews {}", review);
        return reviewService.createReview(review);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        log.info("GET /reviews/" + id);
        return reviewService.getReviewById(id);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) throws ValidationException {
        log.info("PUT /reviews {}", review);
        return reviewService.updateReview(review);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable long reviewId, @PathVariable int userId) {
        log.info("PUT /reviews/{}/like/{}", reviewId, userId);
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable long reviewId, @PathVariable int userId) {
        log.info("PUT /reviews/{}/dislike/{}", reviewId, userId);
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {
        log.info("DELETE /reviews/" + id);
        reviewService.deleteReview(id);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLike(@PathVariable long reviewId, @PathVariable int userId) {
        log.info("DELETE /reviews/{}/like/{}", reviewId, userId);
        reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDislike(@PathVariable long reviewId, @PathVariable int userId) {
        log.info("DELETE /reviews/{}/dislike/{}", reviewId, userId);
        reviewService.deleteDislike(reviewId, userId);
    }
}
