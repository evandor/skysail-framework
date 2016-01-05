package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.response.ResponseWrapper;

import org.restlet.Request;

public interface EntityResourceFilter<T extends Identifiable> {

    public FilterResult beforeHandle(EntityServerResource<T> resource, Request request,
            ResponseWrapper<T> response);

    public FilterResult doHandle(EntityServerResource<T> resource, Request request, ResponseWrapper<T> response);

    public void afterHandle(EntityServerResource<T> resource, Request request, ResponseWrapper<T> response);

}
