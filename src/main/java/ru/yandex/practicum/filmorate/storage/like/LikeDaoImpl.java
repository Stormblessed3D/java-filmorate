package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public Integer getAllLikesByFilmId(Integer filmId) {
        String sqlQuery = "SELECT COUNT(user_id) FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
    }
}
