package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;

import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public interface ListResourceFilter<T> {

    public FilterResult beforeHandle(ListServerResource<T> resource, Request request, ResponseWrapper<T> response);

    public FilterResult doHandle(ListServerResource<T> resource, Request request, ResponseWrapper<T> response);

    public void afterHandle(ListServerResource<T> resource, Request request, ResponseWrapper<T> response);

}
