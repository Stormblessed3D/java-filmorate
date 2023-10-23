package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;
import ru.yandex.practicum.filmorate.service.RatingService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/mpa")
@Validated
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping("/{mpaId}")
    public RatingMPA getMpaById(@PathVariable @Positive Integer mpaId) {
        return ratingService.getMpaById(mpaId);
    }

    @GetMapping
    public List<RatingMPA> getAllMpa() {
        return ratingService.getAllMpa();
    }
}
