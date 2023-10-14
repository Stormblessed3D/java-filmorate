package ru.yandex.practicum.filmorate.storage.like;

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
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LikeDaoImplTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final LikeDao likeDao;

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

        likeDao.addLike(1,1);
        likeDao.addLike(1,2);

        assertEquals(2, likeDao.getAllLikesByFilmId(1));
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

        likeDao.addLike(1,1);
        likeDao.addLike(1,2);
        likeDao.deleteLike(1,2);

        assertEquals(1, likeDao.getAllLikesByFilmId(1));
    }

    @Test
    void getAllLikesByFilmId() {
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

        likeDao.addLike(1,1);
        likeDao.addLike(1,2);

        assertEquals(2, likeDao.getAllLikesByFilmId(1));
    }
}