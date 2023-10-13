package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    void deleteUser(Integer userId);

    void deleteFriend(Integer userId, Integer friendId);

    User updateUser(User user);

    List<User> getAllUsers();

    Optional<User> getUserById(Integer userId);

    List<User> getUserFriends(Integer userId);

    void addFriend(Integer userId, Integer idOfFriendToBeAdded);
}
