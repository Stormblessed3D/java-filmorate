package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.ReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
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
    private Set<Integer> likes;
    public static final int MAXIMUM_DESCRIPTION_LENGTH = 200;
    public static final LocalDate INITIAL_DATE = LocalDate.of(1895, 12, 28);

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        id = 0;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }

    public Integer getNumberOfLikes() {
        if (likes.isEmpty()) {
            return 0;
        }
        return getLikes().size();
    }
}

