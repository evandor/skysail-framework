package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.net.*;

import lombok.extern.slf4j.Slf4j;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

@Slf4j
public class PutRedirectGetFilter<R extends PutEntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            // see PostRedirectGetFilter
            resource.setMetaRefreshTarget(redirectTo);
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
