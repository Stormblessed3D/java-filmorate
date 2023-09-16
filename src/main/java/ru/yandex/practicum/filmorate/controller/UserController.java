package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectVariableException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Data
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getFilmById(@PathVariable Integer userId) {
        if (userId == 0 || userId < 0) {
            throw new IncorrectVariableException("id пользователя не может быть меньше или равно нулю.");
        }
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Integer userId) {
        if (userId == 0 || userId < 0) {
            throw new IncorrectVariableException("id пользователя не может быть меньше или равно нулю.");
        }
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer userId,
                                          @PathVariable Integer otherId) {
        if (userId == 0 || otherId == 0 || userId < 0 || otherId < 0) {
            throw new IncorrectVariableException("id пользователя не может быть меньше или равно нулю.");
        }
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addUserToFriends(@PathVariable Integer userId,
                                 @PathVariable Integer friendId) {
        if (userId == 0 || friendId == 0 || userId < 0 || friendId < 0) {
            throw new IncorrectVariableException("id пользователя не может быть меньше или равно нулю.");
        }
        return userService.addFriend(userId,friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer userId,
                             @PathVariable Integer friendId) {
        if (userId == 0 || friendId == 0 || userId < 0 || friendId < 0) {
            throw new IncorrectVariableException("id пользователя не может быть меньше или равно нулю.");
        }
        return userService.deleteFriend(userId, friendId);
    }
}
