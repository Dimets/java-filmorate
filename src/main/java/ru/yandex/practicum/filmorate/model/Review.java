package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    long reviewId;
    String content;
    boolean isPositive;
    int userId;
    int filmId;
    int useful;
}
