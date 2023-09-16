package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    @Override
    public User addUser(User user) {
        int userId = generateUserId();
        user.setId(userId);
        users.put(userId, user);
        log.info("Пользователь {} добавлен", user.getId());
        return user;
    }

    @Override
    public User deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        users.remove(user.getId());
        log.info("Пользователь c id {} был удален", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        users.put(user.getId(), user);
        log.info("Пользователь c id {} обновлен", user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        return users.get(userId);
    }

    private int generateUserId() {
        return ++userId;
    }
}
