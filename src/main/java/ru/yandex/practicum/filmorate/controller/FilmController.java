package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
@Data
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable @NotNull @Positive Integer filmId) {
        return filmService.getFilmById(filmId);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count) {
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
    public Film addLike(@PathVariable  @NotNull @Positive Integer filmId,
                        @PathVariable  @NotNull @Positive Integer userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable @NotNull @Positive Integer filmId,
                        @PathVariable @NotNull @Positive Integer userId) {
        return filmService.deleteLike(filmId, userId);
    }
}
