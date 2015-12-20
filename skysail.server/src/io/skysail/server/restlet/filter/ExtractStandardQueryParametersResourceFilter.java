package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import de.twenty11.skysail.server.core.restlet.Wrapper;

/**
 * Extracts common query parameters for the skysail framework; those parameters have reserved names
 * starting with an understore, e.g. "_page" or "_filter".
 *
 * <p>If the parameters exist on the query, they are trimmed and added to the request attributes for
 * further consumption.</p>
 */
public class ExtractStandardQueryParametersResourceFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends
        AbstractResourceFilter<R, T> {

    @Override
    protected FilterResult beforeHandle(R resource, Wrapper responseWrapper) {
        addToAttributes(resource, SkysailServerResource.FILTER_PARAM_NAME);
        addToAttributes(resource, SkysailServerResource.PAGE_PARAM_NAME);
        addToAttributes(resource, SkysailServerResource.INSPECT_PARAM_NAME);
        return FilterResult.CONTINUE;
    }

    private void addToAttributes(R resource, String queryKeyName) {
        String filterExpression = resource.getQueryValue(queryKeyName);
        if (filterExpression != null && filterExpression.trim().length() > 0) {
            resource.getRequest().getAttributes().put(queryKeyName, filterExpression.trim());
        }
    }
}
