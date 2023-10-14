package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface FriendDao {
    void deleteFriend(Integer userId, Integer friendId);

    List<User> getUserFriends(Integer userId);

    void addFriend(Integer userId, Integer idOfFriendToBeAdded);

    List<User> getCommonFriends(Integer userId, Integer idOtherUser);
}
