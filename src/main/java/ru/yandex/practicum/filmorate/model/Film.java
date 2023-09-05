package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    public static final int MAXIMUM_DESCRIPTION_LENGTH = 200;
    public static final LocalDate INITIAL_DATE = LocalDate.of(1895, 12, 28);

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        id = 0;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}

