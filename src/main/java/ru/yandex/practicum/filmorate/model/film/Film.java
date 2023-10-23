package ru.yandex.practicum.filmorate.model.film;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Film {
    @EqualsAndHashCode.Include
    private int id;
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200, message = "{validation.description.size.too_long}")
    private String description;
    @NotNull
    @ReleaseDate(message = "Release date should be later than 28-12-1895")
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @NotNull
    private RatingMPA mpa;
    private Set<Genre> genres;
    private Integer rate;
    public static final int MAXIMUM_DESCRIPTION_LENGTH = 200;
    public static final LocalDate INITIAL_DATE = LocalDate.of(1895, 12, 28);

    public Film() {
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        id = 0;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = null;
        this.genres = new HashSet<>();
        rate = 0;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, RatingMPA ratingMPA,
                Set<Genre> genres, Integer rate) {
        id = 0;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = ratingMPA;
        this.genres = genres;
        this.rate = rate;
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, RatingMPA ratingMPA,
                Set<Genre> genres, Integer rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = ratingMPA;
        this.genres = genres;
        this.rate = rate;
    }
}

