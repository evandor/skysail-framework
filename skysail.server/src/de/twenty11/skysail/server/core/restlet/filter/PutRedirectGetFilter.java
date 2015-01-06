package de.twenty11.skysail.server.core.restlet.filter;

import java.net.MalformedURLException;
import java.net.URL;

import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class PutRedirectGetFilter<R extends PutEntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static final Logger logger = LoggerFactory.getLogger(PostRedirectGetFilter.class);

    @Override
    protected void afterHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        String redirectFromQuery = resource.getQuery().getFirstValue("_redirectTo");
        if (redirectFromQuery != null) {
            String path = response.getRequest().getHostRef().toString() + redirectFromQuery;
            try {
                response.redirectSeeOther(new URL(redirectFromQuery).toExternalForm());
                return;
            } catch (MalformedURLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            response.redirectSeeOther(redirectTo);
        }
    }
}
