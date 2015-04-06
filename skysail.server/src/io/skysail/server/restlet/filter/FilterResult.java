package io.skysail.server.restlet.filter;


public enum FilterResult {

    CONTINUE, SKIP, STOP

    /**
     * // TODO Indicates that the request processing should continue normally. If returned from the
     * {@link #beforeHandle(Request, Response)} method, the filter then invokes the {@link #doHandle(Request, Response)}
     * method. If returned from the {@link #doHandle(Request, Response)} method, the filter then invokes the
     * {@link #afterHandle(Request, Response)} method.
     */

    /**
     * // TODO Indicates that after the {@link #beforeHandle(Request, Response)} method, the request processing should
     * skip the {@link #doHandle(Request, Response)} method to continue with the {@link #afterHandle(Request, Response)}
     * method.
     */

    /**
     * // TODO Indicates that the request processing should stop and return the current response from the filter.
     */

}
