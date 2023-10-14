package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);

    boolean deleteFilm(Integer filmId);

    Film updateFilm(Film film);

    Map<Integer, Film> getAllFilms();

    Map<Integer, Film> getMostPopularFilms(int count);

    Film getFilmById(Integer filmId);
}