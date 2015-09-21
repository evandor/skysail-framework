package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.*;

import java.text.ParseException;

import org.restlet.*;
import org.restlet.data.Form;
import org.restlet.routing.Filter;
import org.slf4j.*;

import de.twenty11.skysail.server.core.restlet.*;

/**
 * The abstract base class for Skysail Resource Filters.
 *
 * The approach is similar to what happens in the Restlet {@link Filter} system,
 * but happens after the resource (which handles the request) has been found.
 * Filtering therefore is based on a {@link SkysailServerResource}, the incoming
 * {@link Request} and the outgoing (wrapped) response.
 *
 *
 * @param <R>
 *            a {@link SkysailServerResource} with T as Type Parameter
 * @param <T>
 *            a type representing an Entity
 */
public abstract class AbstractResourceFilter<R extends SkysailServerResource<?>, T extends Identifiable> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractResourceFilter.class);

    private AbstractResourceFilter<R, T> next;

    /**
     * The entry point when using Resource Filters.
     *
     * @param resource
     *            a {@link SkysailServerResource} object
     * @return the result of the processing
     */
    public final ResponseWrapper<T> handle(R resource, Response response) {
        ResponseWrapper<T> responseWrapper = new ResponseWrapper<T>(response);
        handleMe(resource, responseWrapper);
        return responseWrapper;
    }

    public final ListResponseWrapper<T> handleList(R resource, Response response) {
        ListResponseWrapper<T> responseWrapper = new ListResponseWrapper<T>(response);
        handleMe(resource, responseWrapper);
        return responseWrapper;
    }

    /**
     * pre-processing logic, called before the control is passed to the doHandle
     * Method.
     *
     * @param resource
     *            a {@link SkysailServerResource} object
     * @param response
     *            the response to update
     * @return the {@link FilterResult} of the processing, indicating whether to
     *         Continue, Skip or Stop.
     */
    protected FilterResult beforeHandle(R resource, Wrapper responseWrapper) {
        return FilterResult.CONTINUE;
    }

    /**
     * the main processing logic
     *
     * @param resource
     *            a {@link SkysailServerResource} object
     * @param responseWrapper
     *            the response to update
     * @return the {@link FilterResult} of the processing, indicating whether to
     *         Continue, Skip or Stop.
     */
    protected FilterResult doHandle(R resource, Wrapper responseWrapper) {
        AbstractResourceFilter<R, T> next = getNext();
        if (next != null) {
            logger.debug("next filter in chain: {}", next.getClass().getSimpleName());
            next.handleMe(resource, responseWrapper);
        }
        return FilterResult.CONTINUE;
    }

    /**
     * post-processing logic, called before the control is passed to the
     * doHandle Method.
     *
     * @param resource
     *            a {@link SkysailServerResource} object
     * @param response
     *            the response to update
     */
    protected void afterHandle(R resource, Wrapper responseWrapper) {
        // default implementation doesn't do anything
    }

    private final void handleMe(R resource, Wrapper responseWrapper) {
        switch (beforeHandle(resource, responseWrapper)) {
        case CONTINUE:
            switch (doHandle(resource, responseWrapper)) {
            case CONTINUE:
                afterHandle(resource, responseWrapper);
                break;
            case SKIP:
                logger.info("skipping filter chain at filter {}", this.getClass().getSimpleName());
                break;
            case STOP:
                logger.info("stopping filter chain at filter {}", this.getClass().getSimpleName());
                break;
            default:
                break;
            }
            break;
        case SKIP:
            logger.info("skipping filter chain at filter {}", this.getClass().getName());
            afterHandle(resource, responseWrapper);
            break;
        case STOP:
            logger.info("stopping filter chain at filter {}", this.getClass().getName());
            break;
        default:
            throw new IllegalStateException("result from beforeHandle was not in [CONTINUE,SKIP,STOP]");
        }
    }


    public AbstractResourceFilter<R, T> calling(AbstractResourceFilter<R, T> next) {
        AbstractResourceFilter<R, T> lastInChain = getLast();
        lastInChain.setNext(next);
        return this;
    }

    private AbstractResourceFilter<R, T> getNext() {
        return next;
    }

    private AbstractResourceFilter<R, T> getLast() {
        AbstractResourceFilter<R, T> result = this;
        while (result.getNext() != null) {
            result = result.getNext();
        }
        return result;
    }

    public void setNext(AbstractResourceFilter<R, T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        if (getNext() != null) {
            sb.append(" -> ").append(getNext().toString());
        }
        return sb.toString();
    }

    protected Object getDataFromRequest(Request request, R resource) throws ParseException {
        Object entityAsObject = request.getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_ENTITY);
        if (entityAsObject != null) {
            return entityAsObject;
        }
        Form form = (Form) request.getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_FORM);
        if (resource instanceof EntityServerResource) {
            return ((EntityServerResource<T>) resource).getData(form);
        }
        if (resource instanceof PostEntityServerResource) {
            return ((PostEntityServerResource<T>) resource).getData(form);
        }
        if (resource instanceof PutEntityServerResource) {
            return ((PutEntityServerResource<T>) resource).getData(form);
        }

        return null;
    }

}
