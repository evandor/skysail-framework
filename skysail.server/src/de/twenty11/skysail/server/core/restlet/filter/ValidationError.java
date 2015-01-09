package de.twenty11.skysail.server.core.restlet.filter;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

public class ValidationError {

    private String className;
    private String propertyPath;
    private String errorMessage;

    /**
     * creates new set from validations.
     * 
     * @param violations
     * @return errors
     */
    public static Set<ValidationError> fromViolations(Set violations) {
        Set<ValidationError> errors = new HashSet<ValidationError>();

        for (Object o : violations) {
            ConstraintViolation v = (ConstraintViolation) o;

            ValidationError error = new ValidationError();
            error.setClassName(v.getRootBeanClass().getSimpleName());
            error.setErrorMessage(v.getMessage());
            error.setPropertyPath(v.getPropertyPath().toString());
            errors.add(error);
        }

        return errors;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public void setPropertyPath(String propertyPath) {
        this.propertyPath = propertyPath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ValidationError{" + "className='" + className + '\'' + ", propertyPath='" + propertyPath + '\''
                + ", errorMessage='" + errorMessage + '\'' + '}';
    }
}
