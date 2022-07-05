create table IF NOT EXISTS FRIENDS_STATUS
(
    ID          INTEGER auto_increment,
    STATUS_NAME CHARACTER VARYING,
    constraint FRIENDS_STATUS_PK
        primary key (ID)
);

create unique index IF NOT EXISTS FRIENDS_STATUS_ID_UINDEX
    on FRIENDS_STATUS (ID);

create table IF NOT EXISTS GENRE
(
    ID         INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING,
    constraint GENRE_PK
        primary key (ID)
);

create unique index IF NOT EXISTS GENRE_GENRE_NAME_UINDEX
    on GENRE (GENRE_NAME);

create unique index IF NOT EXISTS GENRE_ID_UINDEX
    on GENRE (ID);

create table IF NOT EXISTS RATING_MPA
(
    ID          INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING,
    constraint RATING_MPA_PK
        primary key (ID)
);

create table IF NOT EXISTS FILM
(
    ID            INTEGER auto_increment,
    NAME          CHARACTER VARYING,
    DESCRIPTION   CHARACTER VARYING,
    RELEASE_DATE  DATE,
    DURATION      INTEGER,
    RATING_MPA_ID INTEGER,
    constraint FILM_PK
        primary key (ID),
    constraint FILM_RATING_MPA_ID_FK
        foreign key (RATING_MPA_ID) references RATING_MPA
);

create unique index IF NOT EXISTS FILM_ID_UINDEX
    on FILM (ID);

create table IF NOT EXISTS FILM_GENRE
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRE_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint FILM_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

create unique index IF NOT EXISTS RATING_MPA_ID_UINDEX
    on RATING_MPA (ID);

create table IF NOT EXISTS USERS
(
    ID       INTEGER auto_increment,
    EMAIL    CHARACTER VARYING,
    LOGIN    CHARACTER VARYING,
    NAME     CHARACTER VARYING,
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (ID)
);

create table IF NOT EXISTS FILM_LIKE
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint FILM_LIKE_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint FILM_LIKE_USERS_ID_FK
        foreign key (USER_ID) references USERS
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    STATUS_ID INTEGER,
    constraint FRIENDS_FRIENDS_STATUS_ID_FK
        foreign key (STATUS_ID) references FRIENDS_STATUS,
    constraint FRIENDS_USERS_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDS_USERS_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create unique index IF NOT EXISTS USERS_ID_UINDEX
    on USERS (ID);

