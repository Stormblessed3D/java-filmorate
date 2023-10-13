package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);

    boolean deleteFilm(Integer filmId);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    List<Film> getMostPopularFilms(int count);

    Optional<Film> getFilmById(Integer filmId);

    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(Integer id);

    Optional<RatingMPA> getMpaNameById(Integer id);

    List<RatingMPA> getAllMpa();

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);
}