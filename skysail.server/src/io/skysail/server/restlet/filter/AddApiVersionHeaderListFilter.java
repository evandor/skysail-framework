package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.ResponseWrapper;
import io.skysail.server.utils.HeadersUtils;

import org.osgi.framework.*;
import org.restlet.Response;

public class AddApiVersionHeaderListFilter<R extends SkysailServerResource<?>, T extends Identifiable, S extends java.util.List<T>>
        extends AbstractListResourceFilter<R, T, S> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        if (response.getRequest().getAttributes() == null) {
            return;
        }
        Bundle servingBundle = FrameworkUtil.getBundle(resource.getClass());
        if (servingBundle != null) {
            String version = servingBundle.getHeaders().get("X-Api-Version");
            HeadersUtils.addToHeaders(response, "X-Api-Version", version == null ? "unknown" : version);
        }
    }
}
