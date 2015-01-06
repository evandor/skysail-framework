package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public interface EntityResourceFilter<T> {

    public FilterResult beforeHandle(EntityServerResource<T> resource, Request request,
            ResponseWrapper<T> response);

    public FilterResult doHandle(EntityServerResource<T> resource, Request request, ResponseWrapper<T> response);

    public void afterHandle(EntityServerResource<T> resource, Request request, ResponseWrapper<T> response);

}
