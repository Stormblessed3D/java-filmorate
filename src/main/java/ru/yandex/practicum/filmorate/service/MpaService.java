package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final FilmStorage filmStorage;

    public MpaService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public RatingMPA getMpaById(Integer id) {
        return filmStorage.getMpaNameById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Рейтинг с id %d не найден", id)));
    }

    public List<RatingMPA> getAllMpa() {
        return filmStorage.getAllMpa();
    }
}
