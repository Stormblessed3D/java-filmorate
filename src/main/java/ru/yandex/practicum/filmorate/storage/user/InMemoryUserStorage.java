package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Component("userInMemoryStorage")
@Slf4j
@Data
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
    private int userId = 0;

    @Override
    public User addUser(User user) {
        int userId = generateUserId();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteUser(Integer userId) {
        users.remove(userId);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getUserFriends(Integer userId) {
        return friends.get(userId).stream()
                .map(id -> getUserById(id)
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с id %d не найден", id))))
                .collect(Collectors.toList());
    }

    @Override
    public void addFriend(Integer userId, Integer idOfFriendToBeAdded) {
        if (friends.containsKey(userId)) {
            friends.get(userId).add(idOfFriendToBeAdded);
            return;
        }
        Set<Integer> friendsIds = new HashSet<>();
        friendsIds.add(idOfFriendToBeAdded);
        friends.put(userId, friendsIds);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        Set<Integer> friendsIds = friends.get(userId);
        friendsIds.remove(friendId);
    }

    private int generateUserId() {
        return ++userId;
    }
}
