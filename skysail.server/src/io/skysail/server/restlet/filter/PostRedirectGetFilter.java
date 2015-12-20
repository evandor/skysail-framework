package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.*;

import org.restlet.data.*;
import org.restlet.representation.Variant;

import de.twenty11.skysail.server.core.restlet.Wrapper;

public class PostRedirectGetFilter<R extends PostEntityServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper responseWrapper) {
        String redirectTo = resource.redirectTo();
        Parameter noRedirects = resource.getQuery().getFirst(SkysailServerResource.NO_REDIRECTS);
        if (redirectTo != null && noRedirects == null) {
            Variant variant = (Variant) resource.getRequest().getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_VARIANT);
            if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
                resource.getResponse().redirectSeeOther(redirectTo);
            }
        }
    }
}
