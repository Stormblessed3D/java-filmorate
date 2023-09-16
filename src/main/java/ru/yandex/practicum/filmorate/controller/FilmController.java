package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.IncorrectVariableException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Data
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Integer filmId) {
        if (filmId == 0 || filmId < 0) {
            throw new IncorrectVariableException("id фильма не может быть меньше или равно нулю.");
        }
        return filmService.getFilmById(filmId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count == 0 || count < 0) {
            throw new IncorrectParameterException("количество выводимых фильмов не может быть меньше или равно нулю");
        }
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable Integer filmId,
                        @PathVariable Integer userId) {
        if (filmId == 0 || filmId < 0) {
            throw new IncorrectVariableException("id фильма не может быть меньше или равно нулю.");
        }
        if (userId == 0 || userId < 0) {
            throw new IncorrectVariableException("id пользователя не может быть меньше или равно нулю.");
        }
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Integer filmId,
                        @PathVariable Integer userId) {
        if (filmId == 0 || filmId < 0) {
            throw new IncorrectVariableException("id фильма не может быть меньше или равно нулю.");
        }
        if (userId == 0 || userId < 0) {
            throw new IncorrectVariableException("id пользователя не может быть меньше или равно нулю.");
        }
        return filmService.deleteLike(filmId, userId);
    }
}
