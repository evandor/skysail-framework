package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Response;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class AddRequestIdToResourceFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    public AddRequestIdToResourceFilter() {
    }

    @Override
    protected FilterResult beforeHandle(R resource, ResponseWrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        if (response.getRequest() == null || response.getRequest().getAttributes() == null) {
            return FilterResult.CONTINUE;
        }
        Long requestId = (Long) response.getRequest().getAttributes()
                .get(SkysailServerResource.ATTRIBUTES_INTERNAL_REQUEST_ID);
        if (requestId != null) {
            responseWrapper.setRequestId(requestId);
        }
        return FilterResult.CONTINUE;
    }
}
