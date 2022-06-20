package test.ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {
	InMemoryFilmStorage inMemoryFilmStorage;
	InMemoryUserStorage inMemoryUserStorage;
	UserService userService;
	FilmService filmService

	@BeforeEach
	public void init() {
		inMemoryFilmStorage = new InMemoryFilmStorage();
		inMemoryUserStorage = new InMemoryUserStorage();
		userService = new UserService(inMemoryUserStorage);
		filmService = new FilmService(inMemoryFilmStorage, userService);
	}
	@Test
	void shouldCreateUser() throws ValidationException {
		User user = new User("12345@yandex.ru", "Login", "User name", LocalDate.now());
		userService.create(user);
		Assertions.assertEquals(1, userService.findAll().size());
		Assertions.assertEquals("12345@yandex.ru", userService.findById(1).getEmail());
	}

	@Test
	void shouldValidateUserEmail() {
		User user = new User(null, "Userlogin", "User name", LocalDate.now());
		Exception exception = Assertions.assertThrows(ValidationException.class, () -> userService.create(user));
		Assertions.assertEquals("Email должен содержать @ и не быть пустым",exception.getMessage());

		user.setEmail("");
		exception = Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
		Assertions.assertEquals("Email должен содержать @ и не быть пустым",exception.getMessage());

		user.setEmail("email yandex");
		exception = Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
		Assertions.assertEquals("Email должен содержать @ и не быть пустым",exception.getMessage());
	}


	@Test
	void shouldSuccessValidateFilm() throws ValidationException {
		Film film = new Film("film name","film description", LocalDate.now(),100);
		Assertions.assertTrue(FilmService.validateFilm(film));
	}

	@Test
	void shouldValidateFilmName() {
		Film film = new Film(null,"film description", LocalDate.now(),100);
		Exception exception = Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
		Assertions.assertEquals("Название фильма не может быть пустым",exception.getMessage());
		film.setName("");
		exception = Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
		Assertions.assertEquals("Название фильма не может быть пустым",exception.getMessage());
	}

	@Test
	void shouldValidateFilmDescriptionLength() throws ValidationException {
		String description199 = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
				"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
				"111111111111111";
		String description200 = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
				"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
				"1111111111111111";
		String description201 = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
				"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
				"11111111111111111";
		Film film = new Film("Film name",null, LocalDate.now(),100);
		Assertions.assertTrue(FilmController.validate(film));

		film.setDescription(description199);
		Assertions.assertTrue(FilmController.validate(film));

		film.setDescription(description200);
		Assertions.assertTrue(FilmController.validate(film));

		film.setDescription(description201);
		Exception exception = Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
		Assertions.assertEquals("Максимальная длина описания — 200 символов",exception.getMessage());
	}

	@Test
	void shouldValidateFilmReleaseDate() throws ValidationException {
		Film film = new Film("film name","film description",
				LocalDate.of(1895,12,27),100);
		Exception exception = Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
		Assertions.assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года",exception.getMessage());

		film.setReleaseDate(LocalDate.of(1895, 12, 28));
		Assertions.assertTrue(FilmController.validate(film));

		film.setReleaseDate(LocalDate.of(2022, 12, 28));
		Assertions.assertTrue(FilmController.validate(film));
	}

	@Test
	void shouldValidateFilmDuration() throws ValidationException {
		Film film = new Film("Film name",null, LocalDate.now(),-1);
		Exception exception = Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
		Assertions.assertEquals("Продолжительность фильма должна быть положительной",exception.getMessage());

		film.setDuration(0);
		exception = Assertions.assertThrows(ValidationException.class, () -> FilmController.validate(film));
		Assertions.assertEquals("Продолжительность фильма должна быть положительной",exception.getMessage());

		film.setDuration(1);
		Assertions.assertTrue(FilmController.validate(film));

	}

	@Test
	void shouldSuccessValidateUser() throws ValidationException {
		User user = new User("12345@yandex.ru", "Userlogin", "User name", LocalDate.now());
		Assertions.assertTrue(UserController.validate(user));
	}


	@Test
	void shouldValidateUserLogin() {
		User user = new User("12345@yandex.ru", null, "User name", LocalDate.now());
		Exception exception = Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
		Assertions.assertEquals("Логин не может быть пустым или содержать пробел",exception.getMessage());

		user.setLogin("");
		exception = Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
		Assertions.assertEquals("Логин не может быть пустым или содержать пробел",exception.getMessage());

		user.setLogin("login login");
		exception = Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
		Assertions.assertEquals("Логин не может быть пустым или содержать пробел",exception.getMessage());
	}

	@Test
	void shouldSetEmptyNameWithLoginValue() throws ValidationException {
		User user = new User("12345@yandex.ru", "login", null, LocalDate.now());
		UserController.validate(user);
		Assertions.assertEquals("login", user.getName());
	}

	@Test
	void shouldValidateUserBirthdayInFuture() {
		User user = new User("12345@yandex.ru", "login", "name", LocalDate.now().plusDays(1));
		Exception exception = Assertions.assertThrows(ValidationException.class, () -> UserController.validate(user));
		Assertions.assertEquals("Дата рождения не может быть в будущем",exception.getMessage());

	}
}
