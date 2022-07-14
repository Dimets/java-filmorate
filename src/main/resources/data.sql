insert into GENRE (GENRE_NAME) values ('Комедия');
insert into GENRE (GENRE_NAME) values ('Драма');
insert into GENRE (GENRE_NAME) values ('Мультфильм');
insert into GENRE (GENRE_NAME) values ('Action');
insert into GENRE (GENRE_NAME) values ('Thriller');
insert into GENRE (GENRE_NAME) values ('Doc');


insert into RATING_MPA (RATING_NAME) values ('G');
insert into RATING_MPA (RATING_NAME) values ('PG');
insert into RATING_MPA (RATING_NAME) values ('PG-13');
insert into RATING_MPA (RATING_NAME) values ('R');
insert into RATING_MPA (RATING_NAME) values ('NC-17');

insert into FRIENDS_STATUS (STATUS_NAME)
values ('Requested');
insert into FRIENDS_STATUS (STATUS_NAME)
values ('Accepted');


insert into FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, RATING_MPA_ID)
values ('The Mask', 'Mask film description', '1994-07-29', 101, 4, 3);
insert into FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, RATING_MPA_ID)
values ('Alien', 'Alien film description', '1979-05-25', 115, 2, 4);
insert into FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, RATING_MPA_ID)
values ('Getaway', 'Getaway film description', '2020-04-14', 76, 6, 4);

insert into FILM_GENRE (FILM_ID, GENRE_ID)
values (1, 1);
insert into FILM_GENRE (FILM_ID, GENRE_ID)
values (2, 3);
insert into FILM_GENRE (FILM_ID, GENRE_ID)
values (2, 4);
insert into FILM_GENRE (FILM_ID, GENRE_ID)
values (3, 3);
insert into FILM_GENRE (FILM_ID, GENRE_ID)
values (3, 5);

insert into PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
values ('a@mail.email', 'Alogin', 'Aname', '2000-09-19');
insert into PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
values ('b@mail.email', 'Blogin', 'Bname', '1992-03-24');
insert into PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
values ('c@mail.email', 'Clogin', 'Cname', '1965-12-04');
insert into PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
values ('d@mail.email', 'Dlogin', 'Dname', '1981-02-12');
insert into PUBLIC.USERS (EMAIL, LOGIN, NAME, BIRTHDAY)
values ('e@mail.email', 'Elogin', 'Ename', '1997-07-02');

insert into FILM_LIKE (FILM_ID, USER_ID)
values (1, 1);
insert into FILM_LIKE (FILM_ID, USER_ID)
values (3, 1);
insert into FILM_LIKE (FILM_ID, USER_ID)
values (3, 3);
insert into FILM_LIKE (FILM_ID, USER_ID)
values (3, 4);

insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
values (1, 2, 2);
insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
values (2, 1, 1);
insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
values (1, 3, 2);
insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
values (3, 1, 2);
insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
values (4, 2, 2);
insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
values (4, 3, 2);
insert into FRIENDS (USER_ID, FRIEND_ID, STATUS_ID)
values (5, 2, 2);
