package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;

import java.util.*;
import java.util.stream.Collectors;

@Component("filmInMemoryStorage")
@Data
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();
    private Map<Integer, Set<Integer>> likes = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        int filmId = generateFilmId();
        film.setId(filmId);
        films.put(filmId, film);
        return film;
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        films.remove(filmId);
        return true;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        return Optional.ofNullable(films.get(filmId));
    }

    @Override
    public Optional<Genre> getGenreById(Integer genreId) {
        return Optional.empty();
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Optional<RatingMPA> getMpaNameById(Integer mpaId) {
        return Optional.empty();
    }

    @Override
    public List<RatingMPA> getAllMpa() {
        return null;
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return films.values().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Map<Integer, Set<Integer>> likes = getLikes();
        if (likes.containsKey(filmId)) {
            likes.get(filmId).add(userId);
            return;
        }
        HashSet<Integer> users = new HashSet<>();
        users.add(userId);
        likes.put(filmId, users);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        Map<Integer, Set<Integer>> likes = getLikes();
        if (likes.containsKey(filmId)) {
            likes.get(filmId).remove(userId);
        }
    }

    private int generateFilmId() {
        return ++filmId;
    }
}
