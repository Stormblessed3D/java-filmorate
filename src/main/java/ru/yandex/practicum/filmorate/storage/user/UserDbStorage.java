package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.*;
import java.util.List;

@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        Integer userId = (Integer) keyHolder.getKey();
        user.setId(userId);
        return user;
    }

    @Override
    public void deleteUser(Integer userId) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public User updateUser(User user) {
        try {
            String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;

        } catch (
                EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("User with id of %d not found", user.getId()));
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getUserById(Integer userId) {
        try {
            String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
        } catch (
                EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("User with id of %d not found", userId));
        }
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
