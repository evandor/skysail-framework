package io.skysail.server.restlet.filter;

import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.HeadersUtils;

import java.util.List;
import java.util.stream.Collectors;

import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.util.Series;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;

public class AddLinkheadersFilter<R extends SkysailServerResource<T>, T> extends
        ResponseHeadersManipulatingFilter<R, T> {

    @Override
    protected void afterHandle(R resource, ResponseWrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        if (resource instanceof SkysailServerResource) {
            SkysailServerResource<?> ssr = resource;
            Series<Header> responseHeaders = HeadersUtils.getHeaders(resource.getResponse());
            List<Link> linkheaderAuthorized = ssr.getLinkheaderAuthorized();
            String links = linkheaderAuthorized.stream().map(link -> link.toString(response.getRequest(), ""))
                    .collect(Collectors.joining(","));
            responseHeaders.add(new Header("Link", links));
        }
    }
}
