package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDao likeDao;
    private final GenreDao genreDao;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeDao likeDao, GenreDao genreDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeDao = likeDao;
        this.genreDao = genreDao;
    }

    public List<Film> getAllFilms() {
        return genreDao.addGenresToFilms(filmStorage.getAllFilms());
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        return genreDao.addGenresToFilm(id, film);
    }

    public Film createFilm(Film film) {
        log.info("Фильм был добавлен");
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        log.info("Фильм c id {} был обновлен", film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        return genreDao.addGenresToFilm(updatedFilm.getId(), updatedFilm);
    }

    public void deleteFilm(Integer filmId) {
        getFilmById(filmId);
        if (filmStorage.deleteFilm(filmId)) {
            log.info("Фильм c id {} был удален", filmId);
        }
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        likeDao.addLike(filmId, userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        likeDao.deleteLike(filmId, userId);
        return film;
    }

    public List<Film> getMostPopularFilms(Integer numberOfFilms) {
        return genreDao.addGenresToFilms(filmStorage.getMostPopularFilms(numberOfFilms));
    }
}
