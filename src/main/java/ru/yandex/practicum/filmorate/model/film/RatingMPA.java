package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatingMPA {
    private Integer id;
    private String name;

    public RatingMPA(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public RatingMPA() {
    }
}
