package de.twenty11.skysail.server.core.restlet.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.util.Series;

import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.utils.HeadersUtils;

public class AddLinkheadersFilter<R extends SkysailServerResource<T>, T> extends
        ResponseHeadersManipulatingFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Response response, ResponseWrapper<T> responseWrapper) {
        if (resource instanceof SkysailServerResource) {
            SkysailServerResource<?> ssr = resource;
            Series<Header> responseHeaders = HeadersUtils.getHeaders(resource.getResponse());
            List<Linkheader> linkheaderAuthorized = ssr.getLinkheaderAuthorized();
            String links = linkheaderAuthorized.stream().map(link -> link.toString(response.getRequest(), ""))
                    .collect(Collectors.joining(","));
            responseHeaders.add(new Header("Link", links));
        }
    }
}
