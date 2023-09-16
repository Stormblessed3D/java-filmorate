package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @Override
    public Film addFilm(Film film) {
        int filmId = generateFilmId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Фильм был добавлен");
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new EntityNotFoundException("Фильм не найден");
        }
        films.remove(film.getId());
        log.info("Фильм c id {} был удален", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new EntityNotFoundException("Фильм не найден");
        }
        films.put(film.getId(), film);
        log.info("Фильм c id {} был обновлен", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new EntityNotFoundException(String.format("Фильм с id %d не найден", filmId));
        }
        return films.get(filmId);
    }

    private int generateFilmId() {
        return ++filmId;
    }
}
