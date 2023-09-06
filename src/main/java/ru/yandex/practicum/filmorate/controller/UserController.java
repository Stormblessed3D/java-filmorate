package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Data
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 0;

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        setNameEqualToLoginIfNull(user);
        int userId = generateUserId();
        user.setId(userId);
        users.put(userId, user);
        log.info("Пользователь {} добавлен", user.getId());
        return users.get(userId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с идентификатором {} не зарегистрирован", user.getId());
            throw new EntityNotFoundException("Объект " + user.getClass().getName() + " не зарегистрирован");
        }
        setNameEqualToLoginIfNull(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь c id {} обновлен", user.getId());
        } else {
            createUser(user);
        }
        return user;
    }

    private int generateUserId() {
        return ++userId;
    }

    private void setNameEqualToLoginIfNull(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
