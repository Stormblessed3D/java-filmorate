package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, rating_mpa) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        String sqlQueryId = "SELECT MAX(film_id) FROM films";
        Integer idOfInsertedFilm = jdbcTemplate.queryForObject(sqlQueryId, Integer.class);
        if (film.getGenres() != null) {
            addFilmGenres(film, idOfInsertedFilm);
        }
        return getFilmById(idOfInsertedFilm)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", film.getId())));
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
        return getFilmById(film.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Фильм с id %d не найден", film.getId())));
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT films.film_id, films.name, films.description, films.release_date, films.duration, " +
                "films.rating_mpa, COUNT(likes.user_id) as likes_count\n" +
                "FROM films\n" +
                "LEFT OUTER JOIN likes ON films.film_id = likes.film_id\n" +
                "GROUP BY films.film_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sqlQuery = "SELECT films.film_id, films.name, films.description, films.release_date, films.duration, " +
                "films.rating_mpa, COUNT(likes.user_id) as likes_count\n" +
                "FROM films\n" +
                "LEFT OUTER JOIN likes ON films.film_id = likes.film_id\n" +
                "GROUP BY films.film_id\n" +
                "ORDER BY likes_count DESC\n" +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        try {
            String sqlQuery = "SELECT films.film_id, films.name, films.description, films.release_date, films.duration, " +
                    "films.rating_mpa, COUNT(likes.user_id) AS likes_count " +
                    "FROM films " +
                    "LEFT OUTER JOIN likes ON films.film_id = likes.film_id " +
                    "WHERE films.film_id = ? " +
                    "GROUP BY films.film_id";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId));
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Film with id of %d not found", filmId));
        }
    }

    @Override
    public Optional<RatingMPA> getMpaNameById(Integer mpaId) {
        try {
            String sqlQuery = "SELECT rating_mpa_id, rating_mpa_name FROM rating WHERE rating_mpa_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId));
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("MPA with id of %d not found", mpaId));
        }
    }

    @Override
    public List<RatingMPA> getAllMpa() {
        String sqlQuery = "SELECT rating_mpa_id, rating_mpa_name FROM rating";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Optional<Genre> getGenreById(Integer genreId) {
        try {
            String sqlQuery = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId));
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("Genre with id of %d not found", genreId));
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT genre_id, genre_name FROM genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO likes(film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sqlQueryToDelete = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQueryToDelete, filmId, userId);
    }

    private void addFilmGenres(Film film, Integer filmId) {
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
        String sqlQueryToInsert = "INSERT INTO film_genre(film_id, genre_id) VALUES (?,?)";
        Set<Genre> filmGenres = film.getGenres();
        jdbcTemplate.batchUpdate(
                sqlQueryToInsert,
                filmGenres,
                filmGenres.size(),
                (PreparedStatement ps, Genre genre) -> {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genre.getId());
                }
        );
    }

    private Set<Genre> getGenresByFilmId(Integer filmId) {
        String sqlQuery = "SELECT fg.genre_id, g.genre_name FROM film_genre AS fg " +
                "LEFT OUTER JOIN genres AS g ON fg.genre_id = g.genre_id WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
        return new HashSet<>(genres);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(getMpaNameById(rs.getInt("rating_mpa"))
                        .orElseThrow(() -> new EntityNotFoundException("Рейтинг с id не найден")))
                .genres(getGenresByFilmId(rs.getInt("film_id")))
                .rate(rs.getInt("likes_count"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }

    private RatingMPA mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return RatingMPA.builder()
                .id(rs.getInt("rating_mpa_id"))
                .name(rs.getString("rating_mpa_name"))
                .build();
    }

    protected Integer getAllLikesByFilmId(Integer filmId) {
        String sqlQuery = "SELECT COUNT(user_id) FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
    }
}
