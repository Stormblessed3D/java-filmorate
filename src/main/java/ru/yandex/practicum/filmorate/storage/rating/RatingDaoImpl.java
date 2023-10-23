package ru.yandex.practicum.filmorate.storage.rating;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.film.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RatingDaoImpl implements RatingDao {
    private final JdbcTemplate jdbcTemplate;

    public RatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RatingMPA getMpaNameById(Integer mpaId) {
        try {
            String sqlQuery = "SELECT rating_mpa_id, rating_mpa_name FROM rating WHERE rating_mpa_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("MPA with id of %d not found", mpaId));
        }
    }

    @Override
    public List<RatingMPA> getAllMpa() {
        String sqlQuery = "SELECT rating_mpa_id, rating_mpa_name FROM rating";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    private RatingMPA mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return RatingMPA.builder()
                .id(rs.getInt("rating_mpa_id"))
                .name(rs.getString("rating_mpa_name"))
                .build();
    }
}
