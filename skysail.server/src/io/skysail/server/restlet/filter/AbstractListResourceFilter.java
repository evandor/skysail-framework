package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.*;
import io.skysail.server.restlet.response.ResponseWrapper;

import java.text.ParseException;
import java.util.List;

import org.restlet.*;
import org.restlet.data.Form;
import org.restlet.routing.Filter;
import org.slf4j.*;

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
 *            a type representing an EntityModel
 */
public abstract class AbstractListResourceFilter<R extends SkysailServerResource<?>, T extends Identifiable, S extends List<T>> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractListResourceFilter.class);

    private AbstractListResourceFilter<R, T, S> next;

    /**
     * The entry point when using Resource Filters.
     *
     */
    public final ResponseWrapper<T> handle(R resource, Response response) {
        ResponseWrapper<T> responseWrapper = new ResponseWrapper<T>(response);
        handle(resource, responseWrapper);
        return responseWrapper;
    }

    /**
     * pre-processing logic, called before the control is passed to the doHandle
     * Method.
     *
     */
    protected FilterResult beforeHandle(R resource, ResponseWrapper<T> responseWrapper) {
        return FilterResult.CONTINUE;
    }

    /**
     * the main processing logic
     *
     */
    protected FilterResult doHandle(R resource, ResponseWrapper<T> responseWrapper) {
        AbstractListResourceFilter<R, T, S> next = getNext();
        if (next != null) {
            logger.debug("next filter in chain: {}", next.getClass().getSimpleName());
            next.handle(resource, responseWrapper);
        }
        return FilterResult.CONTINUE;
    }

    /**
     * post-processing logic, called before the control is passed to the
     * doHandle Method.
     *
     */
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        // default implementation doesn't do anything
    }

    private final void handle(R resource, ResponseWrapper<T> responseWrapper) {
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

    public AbstractListResourceFilter<R, T, S> calling(AbstractListResourceFilter<R, T, S> next) {
        AbstractListResourceFilter<R, T, S> lastInChain = getLast();
        lastInChain.setNext(next);
        return this;
    }

    private AbstractListResourceFilter<R, T, S> getNext() {
        return next;
    }

    private AbstractListResourceFilter<R, T, S> getLast() {
        AbstractListResourceFilter<R, T, S> result = this;
        while (result.getNext() != null) {
            result = result.getNext();
        }
        return result;
    }

    public void setNext(AbstractListResourceFilter<R, T, S> next) {
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
