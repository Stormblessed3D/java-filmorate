package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Test
    void addFilm() {
        Film filmToCreate = Film.builder()
                            .name("Film1")
                            .description("Film1Description")
                            .releaseDate(LocalDate.of(2020, 1, 1))
                            .duration(120)
                            .mpa(new RatingMPA(3, "PG-13"))
                            .genres(Set.of(new Genre(2, "Драма")))
                            .rate(6)
                            .build();
        filmStorage.addFilm(filmToCreate);

        assertThat(filmStorage.getFilmById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void deleteFilm() {
        Film filmToCreate = Film.builder()
                .name("Film1")
                .description("Film1Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(6)
                .build();
        filmStorage.addFilm(filmToCreate);
        filmStorage.deleteFilm(1);

        assertEquals(0, filmStorage.getAllFilms().size());
    }

    @Test
    void updateFilm() {
        Film filmToCreate = Film.builder()
                .name("Film1")
                .description("Film1Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(6)
                .build();
        filmStorage.addFilm(filmToCreate);
        filmToCreate.setName("Film1_v2");
        filmToCreate.setId(1);
        filmStorage.updateFilm(filmToCreate);

        assertThat(filmStorage.getFilmById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Film1_v2")
                );

        assertThat(filmStorage.getFilmById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getAllFilms() {
        Film filmToCreate = Film.builder()
                .name("Film1")
                .description("Film1Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(6)
                .build();
        filmStorage.addFilm(filmToCreate);

        assertEquals(1, filmStorage.getAllFilms().size());
    }

    @Test
    void getMostPopularFilms() {
        Film filmToCreate = Film.builder()
                .name("Film1")
                .description("Film1Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(0)
                .build();
        filmStorage.addFilm(filmToCreate);
        Film filmToCreate2 = Film.builder()
                .name("Film2")
                .description("Film2Description")
                .releaseDate(LocalDate.of(2010, 1, 1))
                .duration(66)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(0)
                .build();
        filmStorage.addFilm(filmToCreate2);
        User user1 = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(user1);

        filmStorage.addLike(1,1);

        assertEquals(1, filmStorage.getMostPopularFilms(1).size());
    }

    @Test
    void getFilmById() {
        Film filmToCreate = Film.builder()
                .name("Film1")
                .description("Film1Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(6)
                .build();
        filmStorage.addFilm(filmToCreate);

        Optional<Film> filmOptional = filmStorage.getFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getMpaNameById() {
        assertThat(filmStorage.getMpaNameById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getAllMpa() {
        assertEquals(5, filmStorage.getAllMpa().size());
    }

    @Test
    void getGenreById() {
        assertThat(filmStorage.getGenreById(1))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Комедия")
                );
    }

    @Test
    void getAllGenres() {
        assertEquals(6, filmStorage.getAllGenres().size());
    }

    @Test
    void addLike() {
        Film filmToCreate = Film.builder()
                .name("Film1")
                .description("Film1Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(6)
                .build();
        filmStorage.addFilm(filmToCreate);

        User user1 = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(user1);
        User user2 = new User("user2@gmail.com", "user2", "Fedos",
                LocalDate.of(1980, 2, 15));
        userStorage.addUser(user2);

        filmStorage.addLike(1,1);
        filmStorage.addLike(1,2);

        assertEquals(2, filmStorage.getAllLikesByFilmId(1));
    }

    @Test
    void deleteLike() {
        Film filmToCreate = Film.builder()
                .name("Film1")
                .description("Film1Description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(120)
                .mpa(new RatingMPA(3, "PG-13"))
                .genres(Set.of(new Genre(2, "Драма")))
                .rate(6)
                .build();
        filmStorage.addFilm(filmToCreate);

        User user1 = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(user1);
        User user2 = new User("user2@gmail.com", "user2", "Fedos",
                LocalDate.of(1980, 2, 15));
        userStorage.addUser(user2);

        filmStorage.addLike(1,1);
        filmStorage.addLike(1,2);
        filmStorage.deleteLike(1,2);

        assertEquals(1, filmStorage.getAllLikesByFilmId(1));
    }
}