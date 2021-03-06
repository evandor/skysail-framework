package io.skysail.server.restlet.filter;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.Wrapper;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.HeadersUtils;

public class SetExecutionTimeInResponseFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends
        AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        if (response.getRequest().getAttributes() == null) {
            return;
        }
        Long startTime = (Long) response.getRequest().getAttributes().get("org.restlet.startTime");
        if (startTime == null) {
            return;
        }
        Long duration = System.currentTimeMillis() - startTime;
        HeadersUtils.addToHeaders(response, "X-Duration", duration.toString());
    }
}
