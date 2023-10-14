package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getFilmById(@PathVariable @Positive Integer userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable @Positive Integer userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable @Positive Integer userId,
                                          @PathVariable @Positive Integer otherId) {
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
    public void addUserToFriends(@PathVariable @Positive Integer userId,
                                 @PathVariable @Positive Integer friendId) {
        userService.addFriend(userId,friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Positive Integer userId,
                             @PathVariable @Positive Integer friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}")
    public void deleteFilm(@PathVariable @Positive Integer userId) {
        userService.deleteUser(userId);
    }
}
