package cl.sura.prueba.weatherapi.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { OptionValidator.class })
public @interface IsOptionCorrect {
    String message() default "Option incorrect";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String optionA() default "";

    String optionB() default "";
}
