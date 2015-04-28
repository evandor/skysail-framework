package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.PutEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class PutRedirectGetFilter<R extends PutEntityServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            // see PostRedirectGetFilter
            resource.setMetaRefreshTarget(redirectTo);
        }
    }
}
