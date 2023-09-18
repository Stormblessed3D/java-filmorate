package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
@Data
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getFilmById(@PathVariable @NotNull @Positive Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable @NotNull @Positive Integer userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable @NotNull @Positive Integer userId,
                                          @PathVariable @NotNull @Positive Integer otherId) {
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
    public User addUserToFriends(@PathVariable @NotNull @Positive Integer userId,
                                 @PathVariable @NotNull @Positive Integer friendId) {
        return userService.addFriend(userId,friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable @NotNull @Positive Integer userId,
                             @PathVariable @NotNull @Positive Integer friendId) {
        return userService.deleteFriend(userId, friendId);
    }
}
