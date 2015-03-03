package de.twenty11.skysail.server.core.restlet.filter;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.utils.HeadersUtils;

public class AddApiVersionHeader<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        if (response.getRequest().getAttributes() == null) {
            return;
        }
        Bundle servingBundle = FrameworkUtil.getBundle(resource.getClass());
        String version = servingBundle.getHeaders().get("X-Api-Version");
        HeadersUtils.addToHeaders(response, "X-Api-Version", version == null ? "unknown" : version);
    }
}
