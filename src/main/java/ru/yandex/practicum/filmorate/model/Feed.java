package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Feed {
    private int eventId;
    private String eventType;
    private String operation;
    private Long entityId;
    private int userId;
    private Long timestamp;

    public Feed(String eventType, String operation, Long entityId, int userId, Long timestamp) {
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
        this.userId = userId;
        this.timestamp = timestamp;
    }
}
