package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.model.film.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateConstraintValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate releaseDateField, ConstraintValidatorContext cxt) {
        if (releaseDateField == null) {
            return false;
        }
        return releaseDateField.isAfter(Film.INITIAL_DATE);
    }
}