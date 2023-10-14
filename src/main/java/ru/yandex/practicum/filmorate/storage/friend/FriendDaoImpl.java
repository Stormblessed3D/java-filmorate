package ru.yandex.practicum.filmorate.storage.friend;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FriendDaoImpl implements FriendDao {
    private final JdbcTemplate jdbcTemplate;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        String sqlQuery = "SELECT f.friend_id, u.email, u.login, u.name, u.birthday FROM friends as f " +
                "LEFT OUTER JOIN users AS u ON f.friend_id = u.user_id " +
                "WHERE (f.user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriend, userId);
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

    @Override
    public List<User> getCommonFriends(Integer userId, Integer idOtherUser) {
        String sqlQuery = "SELECT f.friend_id, u.email, u.login, u.name, u.birthday FROM friends as f " +
                "LEFT OUTER JOIN users AS u ON f.friend_id = u.user_id " +
                "WHERE (f.user_id = ?) " +
                "INTERSECT " +
                "SELECT f.friend_id, u.email, u.login, u.name, u.birthday FROM friends as f " +
                "LEFT OUTER JOIN users AS u ON f.friend_id = u.user_id " +
                "WHERE (f.user_id = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriend, userId, idOtherUser);
    }

    private User mapRowToFriend(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("friend_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
