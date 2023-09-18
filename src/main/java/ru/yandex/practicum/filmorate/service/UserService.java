package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
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
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
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

    public void deleteUser(User user) {
        getUserById(user.getId());
        log.info("Пользователь c id {} был удален", user.getId());
        userStorage.deleteUser(user);
    }

    public User addFriend(Integer userId, Integer idOfFriendToBeAdded) {
        User user = getUserById(userId);
        User friendToBeAdded = getUserById(idOfFriendToBeAdded);
        user.getFriends().add(idOfFriendToBeAdded);
        friendToBeAdded.getFriends().add(userId);
        return friendToBeAdded;
    }

    public User deleteFriend(Integer userId, Integer idOfFriendToBeDeleted) {
        User user = getUserById(userId);
        User friendToBeDeleted = getUserById(idOfFriendToBeDeleted);
        user.getFriends().remove(idOfFriendToBeDeleted);
        friendToBeDeleted.getFriends().remove(userId);
        return friendToBeDeleted;
    }

    public List<User> getCommonFriends(Integer userId, Integer idOtherUser) {
        User user = getUserById(userId);
        User otherUser = getUserById(idOtherUser);
        Set<Integer> intersection = new HashSet<>(user.getFriends());
        intersection.retainAll(otherUser.getFriends());
        return intersection.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    private void setNameEqualToLoginIfNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
