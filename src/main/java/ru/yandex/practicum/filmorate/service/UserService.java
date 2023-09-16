package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public List<User> getUserFriends(Integer userId) {
        User user = getUserById(userId);
        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        setNameEqualToLoginIfNull(user);
        return inMemoryUserStorage.addUser(user);
    }

    public User updateUser(User user) {
        setNameEqualToLoginIfNull(user);
        return inMemoryUserStorage.updateUser(user);
    }

    public User deleteUser(User user) {
        return inMemoryUserStorage.deleteUser(user);
    }

    public User addFriend(Integer userId, Integer idOfFriendToBeAdded) {
        User user = inMemoryUserStorage.getUserById(userId);
        user.getFriends().add(idOfFriendToBeAdded);
        User friendToBeAdded = inMemoryUserStorage.getUserById(idOfFriendToBeAdded);
        friendToBeAdded.getFriends().add(userId);
        return friendToBeAdded;
    }

    public User deleteFriend(Integer userId, Integer idOfFriendToBeDeleted) {
        User user = inMemoryUserStorage.getUserById(userId);
        user.getFriends().remove(idOfFriendToBeDeleted);
        User friendToBeDeleted = inMemoryUserStorage.getUserById(idOfFriendToBeDeleted);
        friendToBeDeleted.getFriends().remove(userId);
        return friendToBeDeleted;
    }

    public List<User> getCommonFriends(Integer userId, Integer idOtherUser) {
        User user = inMemoryUserStorage.getUserById(userId);
        User otherUser = inMemoryUserStorage.getUserById(idOtherUser);
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
