package io.skysail.server.restlet.filter;

import io.skysail.api.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.HeadersUtils;

import org.osgi.framework.*;
import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.Wrapper;

public class AddApiVersionHeaderFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper responseWrapper) {
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
