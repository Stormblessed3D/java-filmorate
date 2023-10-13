package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    void addUser() {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(userToCreate);

        assertThat(userStorage.getUserById(1))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void deleteUser() {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(userToCreate);
        userStorage.deleteUser(1);

        assertEquals(0, userStorage.getAllUsers().size());
    }

    @Test
    void updateUser() {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(userToCreate);

        userToCreate.setName("Fedya");
        userToCreate.setId(1);
        userStorage.updateUser(userToCreate);

        assertThat(userStorage.getUserById(1))
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Fedya")
                );
    }

    @Test
    void getAllUsers() {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(userToCreate);

        assertEquals(1, userStorage.getAllUsers().size());
    }

    @Test
    void getUserById() {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(userToCreate);
        Optional<User> userOptional = userStorage.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getUserFriends() {
        User user1 = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(user1);
        User user2 = new User("user2@gmail.com", "user2", "Fedos",
                LocalDate.of(1980, 2, 15));
        userStorage.addUser(user2);

        userStorage.addFriend(1,2);

        assertEquals(1, userStorage.getUserFriends(1).size());
        assertEquals(0, userStorage.getUserFriends(2).size());
    }

    @Test
    void addFriend() {
        User user1 = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(user1);
        User user2 = new User("user2@gmail.com", "user2", "Fedos",
                LocalDate.of(1980, 2, 15));
        userStorage.addUser(user2);

        userStorage.addFriend(1,2);

        assertEquals(1, userStorage.getUserFriends(1).size());
        assertEquals(2, userStorage.getUserFriends(1).get(0).getId());
        assertEquals(0, userStorage.getUserFriends(2).size());
    }

    @Test
    void deleteFriend() {
        User user1 = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userStorage.addUser(user1);
        User user2 = new User("user2@gmail.com", "user2", "Fedos",
                LocalDate.of(1980, 2, 15));
        userStorage.addUser(user2);

        userStorage.addFriend(1,2);
        userStorage.deleteFriend(1,2);

        assertEquals(0, userStorage.getUserFriends(1).size());
    }
}