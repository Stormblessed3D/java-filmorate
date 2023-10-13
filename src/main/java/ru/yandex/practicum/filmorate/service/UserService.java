package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id %d не найден", id)));
    }

    public List<User> getUserFriends(Integer userId) {
        User user = getUserById(userId);
        return userStorage.getUserFriends(userId);
    }

    public User createUser(User user) {
        setNameEqualToLoginIfNull(user);
        log.info("Пользователь {} добавлен", user.getId());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id %d не найден", user.getId())));
        setNameEqualToLoginIfNull(user);
        log.info("Пользователь c id {} обновлен", user.getId());
        return userStorage.updateUser(user);
    }

    public void deleteUser(Integer userId) {
        getUserById(userId);
        log.info("Пользователь c id {} был удален", userId);
        userStorage.deleteUser(userId);
    }

    public void addFriend(Integer userId, Integer idOfFriendToBeAdded) {
        User user = getUserById(userId);
        User friendToBeAdded = getUserById(idOfFriendToBeAdded);
        userStorage.addFriend(userId, idOfFriendToBeAdded);
    }

    public void deleteFriend(Integer userId, Integer idOfFriendToBeDeleted) {
        User user = getUserById(userId);
        User friendToBeDeleted = getUserById(idOfFriendToBeDeleted);
        userStorage.deleteFriend(userId, idOfFriendToBeDeleted);
    }

    public List<User> getCommonFriends(Integer userId, Integer idOtherUser) {
        User user = getUserById(userId);
        User otherUser = getUserById(idOtherUser);
        List<User> userFriends = userStorage.getUserFriends(userId);
        List<User> otherUserFriends = userStorage.getUserFriends(idOtherUser);
        return userFriends.stream()
                .distinct()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toList());
    }

    private void setNameEqualToLoginIfNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
