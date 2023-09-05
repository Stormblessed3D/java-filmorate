package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FilmAlreadyExistsException extends RuntimeException {
    public FilmAlreadyExistsException(String s) {
        super(s);
    }
}
