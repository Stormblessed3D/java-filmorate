package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
    public Film createFilm(@Valid @RequestBody Film film) {
        int filmId = generateFilmId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Фильм {} добавлен", film.getName());
        return films.get(filmId);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с идентификатором {} не зарегистрирован", film.getId());
            throw new EntityNotFoundException("Объект " + film.getClass() + " с идентификатором не зарегистрирован");
        }
        films.put(film.getId(), film);
        log.info("Фильм c id {} обновлен", film.getId());
        return film;
    }

    private int generateFilmId() {
        return ++filmId;
    }
}
