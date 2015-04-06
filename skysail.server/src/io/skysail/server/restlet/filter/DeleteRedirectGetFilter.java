package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.EntityServerResource;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class DeleteRedirectGetFilter<R extends EntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            response.redirectSeeOther(redirectTo);
        }
    }
}
