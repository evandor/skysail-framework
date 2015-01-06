package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.utils.HeadersUtils;

public class SetExecutionTimeInResponseFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        if (response.getRequest().getAttributes() == null) {
            return;
        }
        Long startTime = (Long) response.getRequest().getAttributes().get(
                "org.restlet.startTime");
        if (startTime == null) {
            return;
        }
        Long duration = System.currentTimeMillis() - startTime;
        //Object object = response.getAttributes().get("org.restlet.http.headers");
        //responseWrapper.getSkysailResponse().setExecutionTime(duration);
        HeadersUtils.addToHeaders(response, "X-Duration", duration.toString());
    }
}
