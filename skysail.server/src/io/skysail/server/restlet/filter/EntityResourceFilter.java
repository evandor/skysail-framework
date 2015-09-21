package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.EntityServerResource;

import org.restlet.Request;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public interface EntityResourceFilter<T extends Identifiable> {

    public FilterResult beforeHandle(EntityServerResource<T> resource, Request request,
            ResponseWrapper<T> response);

    public FilterResult doHandle(EntityServerResource<T> resource, Request request, ResponseWrapper<T> response);

    public void afterHandle(EntityServerResource<T> resource, Request request, ResponseWrapper<T> response);

}
