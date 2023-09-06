package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp="\\S*", message = "login should not contain whitespaces")
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        id = 0;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(String email, String login, LocalDate birthday) {
        id = 0;
        this.email = email;
        this.login = login;
        this.name = login;
        this.birthday = birthday;
    }
}
