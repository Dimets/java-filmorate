/*package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.UnknownGenreException;
import ru.yandex.practicum.filmorate.exception.UnknownMpaException;
import ru.yandex.practicum.filmorate.exception.UnknownUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final GenreDao genreDao;
	private final MpaDao mpaDao;
	private final FilmGenreDao filmGenreDao;
	private final FilmLikeDao filmLikeDao;
	private final FriendDao friendDao;

	@Test
	public void testFindUserById() {
		Optional<User> userOptional = userStorage.getUserById(1);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testFindUnknownUserById() {
		Optional<User> userOptional = userStorage.getUserById(-1);

		assertThat(userOptional)
				.isNotPresent();
	}

	@Test
	public void testGetAllUsers() {
		List<User> userList = userStorage.getAllUsers();
		Assertions.assertEquals(5, userList.size());
	}

	@Test
	public void testCreateUser() {
		User user = new User("new_user@email.ru", "LoginNew", "NameNew",
				LocalDate.now().minusYears(10));
		User createdUser = userStorage.createUser(user);
		Assertions.assertEquals(6, userStorage.getAllUsers().size());
		Assertions.assertEquals("new_user@email.ru", createdUser.getEmail());
		Assertions.assertEquals("LoginNew", createdUser.getLogin());
	}

	@Test
	public void testUpdateUser() {
		Optional<User> user = userStorage.getUserById(1);
		User updatedUser = new User("new_user@email.ru", "LoginNewUpdate", "NameNew",
				LocalDate.now().minusYears(10));
		updatedUser.setId(1);

		Optional<User> updateUser = userStorage.updateUser(updatedUser);

		assertThat(updateUser)
				.isPresent()
				.hasValueSatisfying(u ->
						assertThat(u).hasFieldOrPropertyWithValue("login", "LoginNewUpdate")
				);
	}

	@Test
	public void testDeleteUser() {
		int usersCount = userStorage.getAllUsers().size();
		userStorage.deleteUser(1);
		Assertions.assertEquals(usersCount - 1, userStorage.getAllUsers().size());
	}

	@Test
	public void testGetFilmById() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
		Optional<Film> filmOptional = filmStorage.getFilmById(1);

		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testGetAllFilms() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
		List<Film> films = filmStorage.getAllFilms();
		Assertions.assertEquals(3, films.size());

	}

	@Test
	public void testCreateFilm() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
		Genre genre = genreDao.findGenreById(1).get();
		Film film = new Film("New film", "New desc", mpaDao.findMpaById(1).get(),
				LocalDate.now().minusYears(10),100, 1, Set.of(genre));

		Film createdFilm = filmStorage.createFilm(film);

		Assertions.assertEquals(4, filmStorage.getAllFilms().size());
		Assertions.assertEquals("New film", createdFilm.getName());
		Assertions.assertEquals(100, createdFilm.getDuration());
	}

	@Test
	public void testUpdateFilm() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
		Optional<Film> optionalFilm = filmStorage.getFilmById(1);
		Genre genre = genreDao.findGenreById(1).get();
		Film updatedFilm = new Film("Updated film", "New desc", mpaDao.findMpaById(1).get(),
				LocalDate.now().minusYears(10),100, 1, Set.of(genre));
		updatedFilm.setId(1);

		Film updateFilm =filmStorage.updateFilm(updatedFilm);

		Assertions.assertEquals("Updated film", updateFilm.getName());

	}

	@Test
	public void testDeleteFilm() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
		int filmsCount = filmStorage.getAllFilms().size();
		filmStorage.deleteFilm(1);
		Assertions.assertEquals(filmsCount - 1, filmStorage.getAllFilms().size());
	}

	@Test
	public void testFindGenreById() {
		Optional<Genre> genreOptional = genreDao.findGenreById(1);

		assertThat(genreOptional)
				.isPresent()
				.hasValueSatisfying(genre ->
						assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
				);
	}

	@Test
	public void testGetAllGenre() {
		Assertions.assertEquals(6, genreDao.getAllGenre().size());
	}

	@Test
	public void testGetAllMpa() {
		Assertions.assertEquals(5, mpaDao.getAllMpa().size());
	}

	@Test
	public void testFindMpaById() {
		Optional<Mpa> mpaOptional = mpaDao.findMpaById(1);

		assertThat(mpaOptional)
				.isPresent()
				.hasValueSatisfying(mpa ->
						assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
				);
	}

	@Test
	public void testGetFilmGenre() throws UnknownGenreException {
		Assertions.assertEquals(2, filmGenreDao.getFilmGenres(2).size());
	}

	@Test
	public void testDeleteFilmGenre() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
		Optional<Film> film = filmStorage.getFilmById(1);
		filmGenreDao.deleteFilmGenres(film.get());
		Assertions.assertEquals(0, filmGenreDao.getFilmGenres(1).size());
	}

	@Test
	public void testSetFilmGenre() throws UnknownMpaException, UnknownGenreException, UnknownUserException {
		Optional<Film> film = filmStorage.getFilmById(2);
		Set<Genre> genres = filmGenreDao.getFilmGenres(2);

		filmGenreDao.deleteFilmGenres(film.get());
		Assertions.assertEquals(0, filmGenreDao.getFilmGenres(2).size());

		filmGenreDao.setFilmGenres(genres, film.get());
		Assertions.assertEquals(2, filmGenreDao.getFilmGenres(2).size());
	}

	@Test
	public void testGetFilmLikes() {
		Assertions.assertEquals(3, filmLikeDao.getFilmLikes(3).size());
	}

	@Test
	public void testDeleteFilmLike() {
		filmLikeDao.deleteFilmLike(1, 1);
		Assertions.assertEquals(0, filmLikeDao.getFilmLikes(1).size());
	}

	@Test
	public void testAddFilmLike() {
		filmLikeDao.addFilmLike(2,4);
		Assertions.assertEquals(1, filmLikeDao.getFilmLikes(2).size());
	}

	@Test
	public void testGetUserFriends() {
		Assertions.assertEquals(2, friendDao.getUserFriends(4).size());
	}

	@Test
	public void testDeleteFriend() {
		friendDao.deleteFriend(5,2);
		Assertions.assertEquals(0, friendDao.getUserFriends(5).size());
	}

	@Test
	public void testAddFriend() {
		int friendsCount = friendDao.getUserFriends(5).size();
		friendDao.addFriend(5,4);
		Assertions.assertEquals(friendsCount + 1, friendDao.getUserFriends(5).size());
	}
}*/
