package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.*;

public interface GenreDao {
    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(Integer id);

    Film  addGenresToFilm(Integer filmId, Film film);

    ArrayList<Film> addGenresToFilms(Map<Integer, Film> films);
}
