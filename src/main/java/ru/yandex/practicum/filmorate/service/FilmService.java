package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
        return inMemoryFilmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    public Film deleteFilm(Film film) {
        return inMemoryFilmStorage.deleteFilm(film);
    }

    public Film addLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
        film.getLikes().add(userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilmById(filmId);
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
