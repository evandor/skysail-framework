package io.skysail.api.responses;

import java.util.*;

import javax.validation.ConstraintViolation;

import org.apache.commons.lang.Validate;
import org.restlet.Response;
import org.restlet.data.Reference;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

/**
 * Being a "business server", skysail has to deal with business requirements
 * such as constraints (e.g. the field name may not be empty). So when a user
 * requests to add an entity which does not comply with the business rules, you
 * should crete a ConstraintViolationResponse, containing information about the
 * cause and the rules. Skysail utilizes the javax.validation interfaces for
 * this purpose.
 */
public class ConstraintViolationsResponse<T> extends SkysailResponse<T> {

    @Getter
    private Set<ConstraintViolationDetails> violations = new HashSet<ConstraintViolationDetails>();

    @JsonIgnore
    @Getter
    private Reference actionReference;

    public ConstraintViolationsResponse(Response response, T entity,
            @NonNull Set<ConstraintViolation<T>> contraintViolations) {
        super(response, entity);
        Validate.notEmpty(contraintViolations, "Cannot create ConstraintViolationResponse without violations");
        this.actionReference = response.getRequest().getOriginalRef();
        if (contraintViolations != null) {
            for (ConstraintViolation<T> constraintViolation : contraintViolations) {
                violations.add(new ConstraintViolationDetails(constraintViolation));
            }
        }
    }
}
