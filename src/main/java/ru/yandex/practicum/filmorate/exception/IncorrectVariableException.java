package ru.yandex.practicum.filmorate.exception;

public class IncorrectVariableException extends RuntimeException {

    public IncorrectVariableException(final String parameter) {
        super(parameter);
    }
}
