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
    RATE          INTEGER,
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
        foreign key (FILM_ID) references FILM
            on delete cascade,
    constraint FILM_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
            on delete cascade
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

create unique index IF NOT EXISTS USERS_ID_UINDEX
    on USERS (ID);

create table IF NOT EXISTS FILM_LIKE
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint FILM_LIKE_FILM_ID_FK
        foreign key (FILM_ID) references FILM
            on delete cascade,
    constraint FILM_LIKE_USERS_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    STATUS_ID INTEGER,
    constraint FRIENDS_FRIENDS_STATUS_ID_FK
        foreign key (STATUS_ID) references FRIENDS_STATUS,
    constraint FRIENDS_USERS_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade,
    constraint FRIENDS_USERS_ID_FK_2
        foreign key (FRIEND_ID) references USERS
            on delete cascade
);

create table IF NOT EXISTS DIRECTOR
(
    ID            INTEGER auto_increment,
    DIRECTOR_NAME CHARACTER VARYING,
    constraint DIRECTOR_PK
        primary key (ID)
);

create unique index IF NOT EXISTS DIRECTOR_ID_UINDEX
    on DIRECTOR (ID);

create table IF NOT EXISTS FILM_DIRECTOR
(
    FILM_ID     INTEGER,
    DIRECTOR_ID INTEGER,
    constraint FILM_DIRECTOR_DIRECTOR_ID_FK
        foreign key (DIRECTOR_ID) references DIRECTOR,
    constraint FILM_DIRECTOR_FILM_ID_FK
        foreign key (FILM_ID) references FILM
            on delete cascade
);

create table IF NOT EXISTS  FEED_TYPE
(
    ID   INTEGER auto_increment,
    TYPE CHARACTER VARYING,
    constraint FEED_TYPE_PK
        primary key (ID)
);

-- auto-generated definition
create unique index IF NOT EXISTS FEED_TYPE_ID_UINDEX
    on FEED_TYPE (ID);

create table IF NOT EXISTS FEED_OPERATION
(
    ID        integer auto_increment,
    OPERATION character varying,
    constraint FEED_OPERATION_PK
        primary key (ID)
);

create unique index IF NOT EXISTS FEED_OPERATION_ID_UINDEX
    on FEED_OPERATION (ID);

create table IF NOT EXISTS USER_FEED
(
    ID           integer auto_increment,
    CREATE_DTTM  datetime,
    TYPE_ID      integer,
    OPERATION_ID integer,
    ENTITY_ID    integer,
    USER_ID      integer,
    constraint USER_FEED_PK
        primary key (ID),
    constraint USER_FEED_FEED_OPERATION_ID_FK
        foreign key (OPERATION_ID) references FEED_OPERATION,
    constraint USER_FEED_FEED_TYPE_ID_FK
        foreign key (TYPE_ID) references FEED_TYPE,
    constraint USER_FEED_USERS_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

create unique index IF NOT EXISTS USER_FEED_ID_UINDEX
    on USER_FEED (ID);


create table IF NOT EXISTS REVIEW
(
    ID          integer auto_increment,
    CONTENT     character varying,
    IS_POSITIVE boolean,
    USER_ID     integer,
    FILM_ID     integer,
    constraint REVIEW_PK
        primary key (ID),
    constraint REVIEW_FILM_ID_FK
        foreign key (FILM_ID) references FILM
            on delete cascade,
    constraint REVIEW_USERS_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

create unique index IF NOT EXISTS REVIEW_ID_UINDEX
    on REVIEW (ID);

create table IF NOT EXISTS REVIEW_LIKES
(
    REVIEW_ID integer,
    USER_ID   integer,
    IS_USEFUL boolean,
    constraint REVIEW_LIKES_REVIEW_ID_FK
        foreign key (REVIEW_ID) references REVIEW
            on delete cascade,
    constraint REVIEW_LIKES_USERS_ID_FK
        foreign key (USER_ID) references USERS
            on delete cascade
);

