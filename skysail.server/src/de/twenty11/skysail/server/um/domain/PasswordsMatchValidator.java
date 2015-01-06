package de.twenty11.skysail.server.um.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Registration> {

    @Override
    public void initialize(PasswordsMatch passwordParameters) {
    }

    @Override
    public boolean isValid(Registration registration, ConstraintValidatorContext arg1) {
        if (registration.getPassword() == null && registration.getPwdRepeated() == null) {
            return true;
        }
        return registration.getPassword().equals(registration.getPwdRepeated());
    }

}
