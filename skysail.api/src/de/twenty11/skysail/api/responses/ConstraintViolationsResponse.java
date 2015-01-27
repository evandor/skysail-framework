package de.twenty11.skysail.api.responses;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang.Validate;
import org.restlet.data.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Being a "business server", skysail has to deal with business requirements
 * such as constraints (e.g. the field name may not be empty). So when a user
 * requests to add an entity which does not comply with the business rules, you
 * should not create a FailureResponse, but a ConstraintViolationResponse,
 * containing information about the cause and the rules. Skysail utilizes the
 * javax.validation interfaces for this purpose.
 */
public class ConstraintViolationsResponse<T> extends SkysailResponse<T> {

    private Set<ConstraintViolationDetails> violations = new HashSet<ConstraintViolationDetails>();

    @JsonIgnore
    private Reference actionReference;

    public ConstraintViolationsResponse(T entity) {
        super(entity);
    }

    /**
     * Constructor.
     * 
     * @param actionReference
     *            a reference
     * @param entity
     *            an entity
     * @param contraintViolations
     *            a set of violations
     */
    public ConstraintViolationsResponse(Reference actionReference, T entity,
            Set<ConstraintViolation<T>> contraintViolations) {
        super(entity);
        Validate.notNull(contraintViolations);
        Validate.notEmpty(contraintViolations, "Cannot create ConstraintViolationResponse without violations");
        this.entity = entity;
        this.actionReference = actionReference;
        if (contraintViolations != null) {
            for (ConstraintViolation<T> constraintViolation : contraintViolations) {
                violations.add(new ConstraintViolationDetails(constraintViolation));
            }
        }
    }

    /**
     * get violations.
     * 
     * @return violations
     */
    public Set<ConstraintViolationDetails> getViolations() {
        return violations;
    }

    public Reference getActionReference() {
        return actionReference;
    }

}
