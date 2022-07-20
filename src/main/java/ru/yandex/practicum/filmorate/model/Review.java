package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    private long reviewId;
    private String content;
    @JsonProperty("isPositive")
    private Boolean isPositive;
    private int userId;
    private int filmId;
    private int useful;
}
