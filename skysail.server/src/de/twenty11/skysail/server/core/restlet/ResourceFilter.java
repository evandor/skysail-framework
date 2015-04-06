package de.twenty11.skysail.server.core.restlet;

import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.Response;
import org.restlet.resource.Resource;

public abstract class ResourceFilter<T> {

    /**
     * // TODO Indicates that the request processing should continue normally.
     * If returned from the #beforeHandle(Request, Response) method, the filter
     * then invokes the #doHandle(Request, Response) method. If returned from
     * the #doHandle(Request, Response) method, the filter then invokes the
     * #afterHandle(Request, Response) method.
     */
    public static final int CONTINUE = 0;

    /**
     * // TODO Indicates that after the #beforeHandle(Request, Response) method,
     * the request processing should skip the #doHandle(Request, Response)
     * method to continue with the #afterHandle(Request, Response) method.
     */
    public static final int SKIP = 1;

    /**
     * // TODO Indicates that the request processing should stop and return the
     * current response from the filter.
     */
    public static final int STOP = 2;

    private volatile ResourceFilter<T> next;

    public ResourceFilter() {
    }

    public ResourceFilter(ResourceFilter<T> next) {
        this.next = next;
    }

    protected int beforeHandle(Resource resource, Response response, ResponseWrapper<T> responseWrapper) {
        return CONTINUE;
    }

    /**
     * Handles a call. Creates an empty Response object and then invokes
     * #handle(Request, Response).
     * 
     * @param resource
     * @return The returned response.
     */
    public final ResponseWrapper<T> handle(SkysailServerResource<T> resource, Response response) {
        ResponseWrapper<T> responseWrapper = new ResponseWrapper<T>();
        handle(resource, response, responseWrapper);
        return responseWrapper;
    }

    /**
     * Handles a call by first invoking the beforeHandle() method for
     * pre-filtering, then distributing the call to the next Restlet via the
     * doHandle() method. When the handling is completed, it finally invokes the
     * afterHandle() method for post-filtering.
     * 
     * @param resource
     * 
     * @param response
     *            The response to update.
     */
    public final void handle(SkysailServerResource<T> resource, Response response, ResponseWrapper<T> responseWrapper) {
        switch (beforeHandle(resource, response, responseWrapper)) {
        case CONTINUE:
            switch (doHandle(resource, response, responseWrapper)) {
            case CONTINUE:
                afterHandle(resource, response, responseWrapper);
                break;
            default:
                break;
            }
            break;

        default:
            break;
        }
    }

    protected int doHandle(SkysailServerResource<T> resource, Response response, ResponseWrapper<T> responseWrapper) {
        ResourceFilter<T> next = getNext();
        if (next != null) {
            next.handle(resource, response, responseWrapper);
        }
        return CONTINUE;
    }

    protected void afterHandle(Resource resource, Response response, ResponseWrapper responseWrapper) {
    }

    protected ResourceFilter<T> calling(ResourceFilter<T> next) {
        ResourceFilter<T> lastInChain = getLast();
        lastInChain.setNext(next);
        return this;
    }

    public ResourceFilter<T> getNext() {
        return this.next;
    }

    public void setNext(ResourceFilter next) {
        this.next = next;
    }

    private ResourceFilter<T> getLast() {
        ResourceFilter<T> result = this;
        while (result.getNext() != null) {
            result = result.getNext();
        }
        return result;
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
}
