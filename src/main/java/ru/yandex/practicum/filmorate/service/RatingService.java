package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;
import ru.yandex.practicum.filmorate.storage.rating.RatingDao;

import java.util.List;

@Service
@Slf4j
public class RatingService {
    private final RatingDao ratingDao;

    public RatingService(RatingDao ratingDao) {
        this.ratingDao = ratingDao;
    }

    public RatingMPA getMpaById(Integer id) {
        return ratingDao.getMpaNameById(id);
    }

    public List<RatingMPA> getAllMpa() {
        return ratingDao.getAllMpa();
    }
}
