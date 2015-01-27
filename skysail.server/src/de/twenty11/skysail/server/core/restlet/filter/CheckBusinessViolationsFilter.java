package de.twenty11.skysail.server.core.restlet.filter;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;
import org.restlet.data.Status;

import de.twenty11.skysail.api.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.server.core.osgi.OSGiServiceDiscoverer;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

@Slf4j
public class CheckBusinessViolationsFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private final Validator validator;

    /**
     * constructor.
     * 
     * @param cvf
     *            factory
     */
    public CheckBusinessViolationsFilter() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();
        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        // config.messageInterpolator(arg0)
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public FilterResult doHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = responseWrapper.getEntity();
        Set<ConstraintViolation<T>> violations = new HashSet<ConstraintViolation<T>>();
        if (entity != null) {
            violations = validate(entity);
        }
        if (violations.size() > 0) {
            log.info("found {} business validation violation(s): {}", violations.size(), violations.toString());
            responseWrapper.setConstraintViolationResponse(new ConstraintViolationsResponse<T>(response.getRequest()
                    .getOriginalRef(), responseWrapper.getEntity(), violations));
            T skysailResponse = responseWrapper.getEntity();
            // skysailResponse.setData(resource.adaptOnError(Status.CLIENT_ERROR_BAD_REQUEST,
            // entity));
            responseWrapper.setEntity(entity);
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);

            return FilterResult.STOP;
            // ObjectMapper mapper = new ObjectMapper();
            // String validationResultJson;
            // try {
            // validationResultJson =
            // mapper.writeValueAsString(ValidationError.fromViolations(violations));
            // response.setEntity(validationResultJson,
            // MediaType.APPLICATION_JSON);
            // } catch (JsonProcessingException e) {
            // response.setEntity(new StringRepresentation(e.getMessage()));
            // }
            // throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
            // "Business violations");
        }
        super.doHandle(resource, response, responseWrapper);
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
