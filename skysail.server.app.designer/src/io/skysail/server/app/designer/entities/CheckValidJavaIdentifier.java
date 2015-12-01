package io.skysail.server.app.designer.entities;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

import javax.validation.*;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckValidJavaIdentifierValidator.class)
@Documented
public @interface CheckValidJavaIdentifier {

    String message() default "{com.mycompany.constraints.checkcase}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // String value();

}