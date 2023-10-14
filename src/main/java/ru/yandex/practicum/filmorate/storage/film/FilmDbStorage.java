package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.rating.RatingDao;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final GenreDao genreDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDao genreDao, RatingDao ratingDao, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, rating_mpa) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Integer filmId = (Integer) keyHolder.getKey();
        film.setId(filmId);
        if (film.getGenres() != null) {
            addGenresToFilm(film, filmId);
        }
        return film;
    }

    @Override
    public boolean deleteFilm(Integer filmId) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId) > 0;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_mpa = ? WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() != null) {
            updateFilmGenres(film);
        }
        return getFilmById(film.getId());
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        String sqlQuery = "SELECT films.film_id, films.name, films.description, films.release_date, films.duration, " +
                "films.rating_mpa, rating.rating_mpa_name AS rating_name, COUNT(likes.user_id) as likes_count\n" +
                "FROM films\n" +
                "LEFT OUTER JOIN likes ON films.film_id = likes.film_id\n" +
                "LEFT OUTER JOIN rating ON films.rating_mpa = rating.rating_mpa_id\n" +
                "GROUP BY films.film_id";

        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Film film = mapRowToFilm(rs, rowNum);
            films.put(film.getId(), film);
            return film;
        });
        return films;
    }

    @Override
    public Map<Integer, Film> getMostPopularFilms(int count) {
        String sqlQuery = "SELECT films.film_id, films.name, films.description, films.release_date, films.duration, " +
                "films.rating_mpa, rating.rating_mpa_name AS rating_name, COUNT(likes.user_id) as likes_count\n" +
                "FROM films\n" +
                "LEFT OUTER JOIN likes ON films.film_id = likes.film_id\n" +
                "LEFT OUTER JOIN rating ON films.rating_mpa = rating.rating_mpa_id\n" +
                "GROUP BY films.film_id\n" +
                "ORDER BY likes_count DESC\n" +
                "LIMIT ?";

        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            Film film = mapRowToFilm(rs, rowNum);
            films.put(film.getId(), film);
            return film;
        }, count);
        return films;
    }

    @Override
    public Film getFilmById(Integer filmId) {
        try {
            String sqlQuery = "SELECT films.film_id, films.name, films.description, films.release_date, films.duration, " +
                    "films.rating_mpa, rating.rating_mpa_name AS rating_name, COUNT(likes.user_id) AS likes_count " +
                    "FROM films " +
                    "LEFT OUTER JOIN likes ON films.film_id = likes.film_id " +
                    "LEFT OUTER JOIN rating ON films.rating_mpa = rating.rating_mpa_id\n" +
                    "WHERE films.film_id = ? " +
                    "GROUP BY films.film_id";

            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Film with id of %d not found", filmId));
        }
    }

    private void addGenresToFilm(Film film, Integer filmId) {
        String sqlQueryToInsert = "INSERT INTO film_genre(film_id, genre_id) VALUES (?,?)";
        Set<Genre> filmGenres = film.getGenres();
        jdbcTemplate.batchUpdate(
                sqlQueryToInsert,
                filmGenres,
                filmGenres.size(),
                (PreparedStatement ps, Genre genre) -> {
                    ps.setInt(1, filmId);
                    ps.setInt(2, genre.getId());
                }
        );
    }

    private void updateFilmGenres(Film film) {
        String sqlQueryToDelete = "DELETE FROM film_genre WHERE film_id = ?";
        if (film.getGenres().size() == 0) {
            jdbcTemplate.update(sqlQueryToDelete, film.getId());
            return;
        }
        jdbcTemplate.update(sqlQueryToDelete, film.getId());
        addGenresToFilm(film, film.getId());
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new RatingMPA(rs.getInt("rating_mpa"), rs.getString("rating_name")))
                .genres(new HashSet<>())
                .rate(rs.getInt("likes_count"))
                .build();
    }
}

