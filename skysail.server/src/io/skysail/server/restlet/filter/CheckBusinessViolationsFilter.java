package io.skysail.server.restlet.filter;

import java.util.*;

import javax.validation.*;

import org.restlet.Response;
import org.restlet.data.Status;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.api.responses.ConstraintViolationsResponse;
import io.skysail.api.validation.ValidatorService;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckBusinessViolationsFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private final Validator validator;

    /**
     * constructor.
     *
     * @param application
     *
     */
    public CheckBusinessViolationsFilter(SkysailApplication application) {
        ValidatorService validatorService = application.getValidatorService();
        if (validatorService == null) {
            throw new IllegalStateException("no validatorService found");
        }
        validator = validatorService.getValidator();
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Object entity = responseWrapper.getEntity();
        Set<ConstraintViolation<T>> violations = new HashSet<ConstraintViolation<T>>();
        if (entity != null) {
            violations = validate((T)entity);
        }
        Response response = responseWrapper.getResponse();
        if (!violations.isEmpty()) {
            log.info("found {} business validation violation(s): {}", violations.size(), violations.toString());
            responseWrapper.setConstraintViolationResponse(new ConstraintViolationsResponse<T>(response, (T)responseWrapper.getEntity(), violations));
            responseWrapper.setEntity(entity);
            resource.setCurrentEntity(entity);
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            response.getHeaders().add("X-Status-Reason", "Validation failed");

            return FilterResult.STOP;
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        ClassLoader thisClassLoader = this.getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(thisClassLoader);

        Set<ConstraintViolation<T>> violations = validator.validate(entity);

        Thread.currentThread().setContextClassLoader(ccl);
        if (violations.size() > 0) {
            log.warn("constraint violations found on {}: {}", entity, violations);
        }
        return violations;
    }

}
