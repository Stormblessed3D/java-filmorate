package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
        filmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", film.getId())));
        log.info("Фильм c id {} был обновлен", film.getId());
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Film film) {
        filmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", film.getId())));
        log.info("Фильм c id {} был удален", film.getId());
        filmStorage.deleteFilm(film);
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        film.getLikes().remove(userId);
        return film;
    }

    public List<Film> getMostPopularFilms(Integer numberOfFilms) {
        return getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getNumberOfLikes).reversed())
                .limit(numberOfFilms)
                .collect(Collectors.toList());
    }
}
