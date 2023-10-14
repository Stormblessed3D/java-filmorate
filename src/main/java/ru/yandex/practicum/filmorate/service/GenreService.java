package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getGenreById(Integer id) {
        return genreDao.getGenreById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Жанр с id %d не найден", id)));
    }

    public List<Genre> getAllGenres() {
        return genreDao.getAllGenres();
    }
}
