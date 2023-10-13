package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        String sqlQueryId = "SELECT MAX(user_id) FROM users";
        Integer idOfInsertedUser = jdbcTemplate.queryForObject(sqlQueryId, Integer.class);
        return getUserById(idOfInsertedUser)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id %d не найден", user.getId())));
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
            return getUserById(user.getId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id %d не найден",
                            user.getId())));
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
    public Optional<User> getUserById(Integer userId) {
        try {
            String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId));
        } catch (
                EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(String.format("User with id of %d not found", userId));
        }
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users " +
                "WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public void addFriend(Integer userId, Integer idOfFriendToBeAdded) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, idOfFriendToBeAdded);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
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
