package ru.yandex.practicum.filmorate.annotation;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateConstraintValidator implements ConstraintValidator<ReleaseDate, LocalDate> {

/*    private LocalDate annotationReleaseDate;

    @Override
    public void initialize(ReleaseDate releaseDate) {
    }*/

    @Override
    public boolean isValid(LocalDate releaseDateField, ConstraintValidatorContext cxt) {
        if (releaseDateField == null) {
            return false;
        }
        return releaseDateField.isAfter(Film.INITIAL_DATE);
    }
}