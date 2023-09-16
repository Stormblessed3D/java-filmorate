package ru.yandex.practicum.filmorate.exception;

public class IncorrectParameterException extends RuntimeException {

    public IncorrectParameterException(final String parameter) {
        super(parameter);
    }
}
