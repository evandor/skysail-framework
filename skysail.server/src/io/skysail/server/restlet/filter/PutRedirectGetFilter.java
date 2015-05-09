package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.*;

import java.net.*;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;
import org.restlet.data.*;
import org.restlet.representation.Variant;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

@Slf4j
public class PutRedirectGetFilter<R extends PutEntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        String redirectTo = resource.redirectTo();
        Parameter noRedirects = resource.getQuery().getFirst(SkysailServerResource.NO_REDIRECTS);
        if (redirectTo != null && noRedirects == null) {
            Variant variant = (Variant) resource.getRequest().getAttributes().get(SkysailServerResource.SKYSAIL_SERVER_RESTLET_VARIANT);
            if (MediaType.TEXT_HTML.equals(variant.getMediaType())) {
                resource.getResponse().redirectSeeOther(redirectTo);
                return;
            }
        }
        
        Response response = responseWrapper.getResponse();
        String redirectFromQuery = resource.getQuery() != null ? resource.getQuery().getFirstValue("_redirectTo")
                : null;
        if (redirectFromQuery != null) {
            try {
                response.redirectSeeOther(new URL(redirectFromQuery).toExternalForm());
                return;
            } catch (MalformedURLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
