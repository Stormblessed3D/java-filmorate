package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendDao friendDao;

    public UserService(UserStorage userStorage, FriendDao friendDao) {
        this.userStorage = userStorage;
        this.friendDao = friendDao;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUserFriends(Integer userId) {
        User user = getUserById(userId);
        return friendDao.getUserFriends(userId);
    }

    public User createUser(User user) {
        setNameEqualToLoginIfNull(user);
        log.info("Пользователь {} добавлен", user.getId());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId());
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
        friendDao.addFriend(userId, idOfFriendToBeAdded);
    }

    public void deleteFriend(Integer userId, Integer idOfFriendToBeDeleted) {
        User user = getUserById(userId);
        User friendToBeDeleted = getUserById(idOfFriendToBeDeleted);
        friendDao.deleteFriend(userId, idOfFriendToBeDeleted);
    }

    public List<User> getCommonFriends(Integer userId, Integer idOtherUser) {
        User user = getUserById(userId);
        User otherUser = getUserById(idOtherUser);
        return friendDao.getCommonFriends(userId, idOtherUser);
    }

    private void setNameEqualToLoginIfNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
