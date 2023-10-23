package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    private Integer id;
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
    }
}
