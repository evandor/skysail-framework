package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class DeleteRedirectGetFilter<R extends EntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            response.redirectSeeOther(redirectTo);
        }
    }
}