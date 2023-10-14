package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;

@Repository
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
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
    public Film addGenresToFilm(Integer filmId, Film film) {
        String sqlQuery = "SELECT fg.genre_id, g.genre_name FROM film_genre AS fg " +
                "LEFT OUTER JOIN genres AS g ON fg.genre_id = g.genre_id WHERE fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    @Override
    public ArrayList<Film> addGenresToFilms(Map<Integer, Film> films) {
        Collection<Integer> filmsId = films.keySet();
        String sqlQuery = "SELECT fg.film_id, fg.genre_id, g.genre_name FROM film_genre AS fg " +
                "LEFT OUTER JOIN genres AS g ON fg.genre_id = g.genre_id WHERE fg.film_id IN (:filmsId)";

        SqlParameterSource parameters = new MapSqlParameterSource("filmsId", filmsId);

        namedJdbcTemplate.query(sqlQuery, parameters, rs -> {
            Film film = films.get(rs.getInt("film_id"));
            Set<Genre> genres = film.getGenres();
            genres.add(Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("genre_name"))
                    .build());
            film.setGenres(genres);
            films.put(film.getId(), film);
        });
        return new ArrayList<>(films.values());
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();
    }
}
