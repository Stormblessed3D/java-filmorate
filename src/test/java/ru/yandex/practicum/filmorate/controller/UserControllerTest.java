package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_thenResponseStatusOk_WithFilmsCollectionInBody() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userToCreate.setId(1);
        List<User> expectedUsers = List.of(userToCreate);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk());

        String response = mockMvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(expectedUsers), response);
    }

    @Test
    void createUser_whenInvokedWithValidFilm_thenResponseStatusOk_WithCreatedUserInBody() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userToCreate.setId(1);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userToCreate), response);
    }

    @Test
    void createUser_whenRequestIsEmpty_thenReturnBadRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_whenNameIsBlank_thenResponseStatusOk() throws Exception {
        User userToCreate = new User("friend@common.ru", "common",
                LocalDate.of(2000, 8, 20));

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        userToCreate.setId(1);
        assertEquals(objectMapper.writeValueAsString(userToCreate), response);
    }

    @Test
    void createUser_whenEmailIsBlank_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userToCreate.setEmail("");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_whenEmailDoesNotContainAtSymbol_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUsergmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_whenLoginIsEmpty_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userToCreate.setLogin("");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_whenLoginIncludesBlanks_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUsergmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userToCreate.setLogin("top User");

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_whenBirthDateIsInFuture_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        userToCreate.setBirthday(LocalDate.of(2030,1,1));

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_whenInvokedWithValidFilm_thenResponseStatusOk_WithCreatedUserInBody() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)));

        userToCreate.setName("TestUserUpdated");
        userToCreate.setId(1);

        String response = mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userToCreate), response);
    }

    @Test
    void updateUser_whenRequestBodyIsEmpty_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)));

        mockMvc.perform(put("/users")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_whenEmailIsBlank_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)));

        userToCreate.setEmail("");
        userToCreate.setId(1);

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_whenEmailDoesNotContainAtSymbol_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)));

        userToCreate.setEmail("topUsergmail.com");
        userToCreate.setId(1);

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_whenLoginIsEmpty_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)));

        userToCreate.setLogin("");
        userToCreate.setId(1);

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_whenLoginIncludesBlanks_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)));

        userToCreate.setLogin("top User");
        userToCreate.setId(1);

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_whenBirthDateIsInFuture_thenReturnBadRequest() throws Exception {
        User userToCreate = new User("topUser@gmail.com", "topUser", "Alex",
                LocalDate.of(1990, 1, 1));
        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(userToCreate)));

        userToCreate.setBirthday(LocalDate.of(2030,1,1));
        userToCreate.setId(1);

        mockMvc.perform(put("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isBadRequest());
    }
}