package de.twenty11.skysail.server.app.profile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, ChangePasswordEntity> {

    @Override
    public void initialize(PasswordsMatch arg0) {
    }

    @Override
    public boolean isValid(ChangePasswordEntity entity, ConstraintValidatorContext arg1) {
        if (entity.getPassword() == null && entity.getPwdRepeated() == null) {
            return true;
        }
        return entity.getPassword().equals(entity.getPwdRepeated());
    }
}