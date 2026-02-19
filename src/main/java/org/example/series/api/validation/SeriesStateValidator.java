package org.example.series.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.series.api.dto.SeriesRequest;

import java.time.Year;

/**
 * Bean validation helper for validating series state constraints.
 */
public class SeriesStateValidator implements ConstraintValidator<ValidSeriesState, SeriesRequest> {

    @Override
    public boolean isValid(SeriesRequest request, ConstraintValidatorContext context) {

        if (request == null) return true;

        Boolean finished = request.getFinished(); // <-- тут Boolean
        if (finished == null) return true;        // @NotNull зловить це окремо

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        // Rule 1: finished series cannot be from the future
        if (Boolean.TRUE.equals(finished) && request.getYear() > Year.now().getValue()) {
            context.buildConstraintViolationWithTemplate("Finished series cannot be in the future")
                    .addPropertyNode("year")
                    .addConstraintViolation();
            valid = false;
        }

        // Rule 2: if finished = false and year too old → illogical
        if (Boolean.FALSE.equals(finished) && request.getYear() < 1950) {
            context.buildConstraintViolationWithTemplate("Ongoing series cannot start before 1950")
                    .addPropertyNode("finished")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
