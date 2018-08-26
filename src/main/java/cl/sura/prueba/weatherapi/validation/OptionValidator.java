package cl.sura.prueba.weatherapi.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OptionValidator implements ConstraintValidator<IsOptionCorrect, String> {

    private String optionA;
    private String optionB;

    @Override
    public void initialize(IsOptionCorrect constraintAnnotation) {
        this.optionA = constraintAnnotation.optionA();
        this.optionB = constraintAnnotation.optionB();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (value == null || value.isEmpty() || (!optionA.equalsIgnoreCase(value) && !optionB.equalsIgnoreCase(value))) {
            return false;
        }

        return true;
    }
}
