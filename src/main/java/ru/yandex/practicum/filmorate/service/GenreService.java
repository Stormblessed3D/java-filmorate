package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final FilmStorage filmStorage;

    public GenreService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Genre getGenreById(Integer id) {
        return filmStorage.getGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Жанр с id %d не найден", id)));
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }
}
