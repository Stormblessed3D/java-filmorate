package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.film.RatingMPA;

import java.util.List;

public interface RatingDao {
    RatingMPA getMpaNameById(Integer id);

    List<RatingMPA> getAllMpa();
}
