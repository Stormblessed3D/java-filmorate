package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmDoesNotExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Data
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 0;

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody @NonNull Film film) {
        try {
            validate(film);
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            throw e;
        }
        int filmId = generateFilmId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @NonNull Film film) {
        try {
            if (!films.containsKey(film.getId())) {
                throw new FilmDoesNotExistsException("Фильм с идентификатором " +
                        film.getId() + " не зарегистрирован.");
            }
        } catch (FilmDoesNotExistsException e) {
            log.warn(e.getMessage(), e);
            throw e;
        }
        try {
            validate(film);
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            throw e;
        }
        films.put(film.getId(), film);
        log.info("Фильм c id {} обновлен", film.getId());
        return film;
    }

    private void validate(Film film) {
        boolean isBlank = film.getName().isBlank();
        boolean isLengthExceeded = film.getDescription().length() > Film.MAXIMUM_DESCRIPTION_LENGTH;
        boolean isReleaseDateBeforeInitialDate = film.getReleaseDate().isBefore(Film.INITIAL_DATE);
        boolean isDurationNegative = film.getDuration() < 0;
        if (isBlank || isLengthExceeded || isReleaseDateBeforeInitialDate || isDurationNegative) {
            throw new ValidationException("Фильм не соответствует необходимым критериям по названию, " +
                    "максимальной длине описания, дате релиза либо продолжительности");
        }
    }

    private int generateFilmId() {
        return ++filmId;
    }
}
