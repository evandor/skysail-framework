package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.Wrapper;

public class RedirectFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper responseWrapper) {
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            Response response = responseWrapper.getResponse();
            response.redirectSeeOther(redirectTo);
        }
    }
}
