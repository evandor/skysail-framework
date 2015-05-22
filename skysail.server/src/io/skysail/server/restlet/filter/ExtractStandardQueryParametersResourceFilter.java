package io.skysail.server.restlet.filter;

import io.skysail.server.restlet.resources.SkysailServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class ExtractStandardQueryParametersResourceFilter<R extends SkysailServerResource<T>, T> extends
        AbstractResourceFilter<R, T> {

    @Override
    protected FilterResult beforeHandle(R resource, ResponseWrapper<T> responseWrapper) {
        String filterExpression = resource
                .getQueryValue(SkysailServerResource.SKYSAIL_SERVER_RESTLET_FILTER_PARAM_NAME);
        if (filterExpression != null && filterExpression.trim().length() > 0) {
            resource.getRequest().getAttributes().put(SkysailServerResource.SKYSAIL_SERVER_RESTLET_FILTER_PARAM_VALUE, filterExpression.trim());
        }
        return FilterResult.CONTINUE;
    }
}
