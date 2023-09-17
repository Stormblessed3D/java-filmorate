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
    private final FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        return inMemoryFilmStorage.getFilmById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", id)));
    }

    public Film createFilm(Film film) {
        log.info("Фильм был добавлен");
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        inMemoryFilmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", film.getId())));
        log.info("Фильм c id {} был обновлен", film.getId());
        return inMemoryFilmStorage.updateFilm(film);
    }

    public void deleteFilm(Film film) {
        inMemoryFilmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", film.getId())));
        log.info("Фильм c id {} был удален", film.getId());
        inMemoryFilmStorage.deleteFilm(film);
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
