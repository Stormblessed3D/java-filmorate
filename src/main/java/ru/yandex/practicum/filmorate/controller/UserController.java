package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
    public User createUser(@RequestBody @NonNull User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        try {
            validate(user);
        } catch (ValidationException e) {
            log.warn(e.getMessage(), e);
            throw e;
        }
        int userId = generateUserId();
        user.setId(userId);
        users.put(userId, user);
        log.info("Пользователь {} добавлен", user.getId());
        return users.get(userId);
    }

    @PutMapping
    public User updateUser(@RequestBody @NonNull User user) {
        try {
            if (!users.containsKey(user.getId())) {
                throw new UserDoesNotExistException("Пользователь с идентификатором " +
                        user.getId() + " не зарегистрирован.");
            }
        } catch (UserDoesNotExistException e) {
            log.warn(e.getMessage(), e);
            throw e;
        }
        try {
            validate(user);
        } catch (ValidationException e) {
            log.warn(e.getMessage(),e);
            throw e;
        }

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь c id {} обновлен", user.getId());
        } else {
            createUser(user);
        }
        return user;
    }

    private void validate(User user) {
        boolean isEmailIncorrect = user.getEmail().isBlank() || !user.getEmail().contains("@");
        boolean isLoginIncorrect = user.getLogin().isBlank() || StringUtils.containsWhitespace(user.getLogin());
        boolean isBirthDateInFuture = user.getBirthday().isAfter(LocalDate.now());
        if (isEmailIncorrect || isLoginIncorrect || isBirthDateInFuture) {
            throw new ValidationException("Пользователь не соответствует необходимым критериям по email, " +
                    "логину или дате рождения");
        }
    }

    private int generateUserId() {
        return ++userId;
    }
}
