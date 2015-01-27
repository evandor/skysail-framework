package de.twenty11.skysail.api.responses;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang.Validate;

public class ConstraintViolationDetails {

    private String propertyPath;
    private String message;

    public ConstraintViolationDetails() {
        // for jackson
    }

    /**
     * constructor.
     * 
     * @param constraintViolation
     *            a violation
     */
    public ConstraintViolationDetails(ConstraintViolation<?> constraintViolation) {
        Validate.notNull(constraintViolation, "constraintValidation is null when creating ConstraintValidationDetails");
        propertyPath = constraintViolation.getPropertyPath().toString();
        message = constraintViolation.getMessage();
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public String getMessage() {
        return message;
    }
}
