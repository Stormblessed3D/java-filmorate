package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.controller.ErrorResponse;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice(value = "ru.yandex.practicum.filmorate.controller")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(ValidationException e) {
        log.warn("Ошибка валидации - получен статус 400 Bad request {}", e.getMessage(), e);
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации - получен статус 400 Bad request {}", e.getMessage(), e);
        return new ErrorResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(ConstraintViolationException e) {
        log.warn("Ошибка валидации - получен статус 400 Bad request {}", e.getMessage(), e);
        return new ErrorResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(EntityNotFoundException e) {
        log.warn("Объект не найден - получен статус 404 Not found {}", e.getMessage(), e);
        return new ErrorResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(Throwable e) {
        log.warn("Получен статус 500 Internal ServerError {}", e.getMessage(), e);
        return new ErrorResponse("Внутренняя ошибка сервера", e.getMessage());
    }
}
