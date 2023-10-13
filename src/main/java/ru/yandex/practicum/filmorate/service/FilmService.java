package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", id)));
    }

    public Film createFilm(Film film) {
        log.info("Фильм был добавлен");
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        log.info("Фильм c id {} был обновлен", film.getId());
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Integer filmId) {
        getFilmById(filmId);
        if (filmStorage.deleteFilm(filmId)) {
            log.info("Фильм c id {} был удален", filmId);
        }
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId)
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId)));
        filmStorage.addLike(filmId, userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId)));
        filmStorage.deleteLike(filmId, userId);
        return film;
    }

    public List<Film> getMostPopularFilms(Integer numberOfFilms) {
        return filmStorage.getMostPopularFilms(numberOfFilms);
    }
}
