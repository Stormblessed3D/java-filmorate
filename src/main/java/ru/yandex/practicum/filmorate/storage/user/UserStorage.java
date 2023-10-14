package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    void deleteUser(Integer userId);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Integer userId);
}
