package io.skysail.server.restlet.filter;

import io.skysail.api.links.Link;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.util.Series;

import de.twenty11.skysail.server.core.restlet.Wrapper;

public class AddLinkheadersFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper responseWrapper) {
        Response response = responseWrapper.getResponse();
        if (resource instanceof SkysailServerResource) {
            SkysailServerResource<?> ssr = resource;
            Series<Header> responseHeaders = HeadersUtils.getHeaders(resource.getResponse());
            List<Link> linkheaderAuthorized = ssr.getAuthorizedLinks();
            linkheaderAuthorized.forEach(getPathSubstitutions(resource));
            String links = linkheaderAuthorized.stream().map(link -> link.toString(""))
                    .collect(Collectors.joining(","));
            responseHeaders.add(new Header("Link", links));
        }
    }

    /**
     * example: l -&gt; { l.substitute("spaceId", spaceId).substitute("id",
     * getData().getPage().getRid()); };
     * @param resource
     *
     * @return consumer for pathSubs
     */
    private Consumer<? super Link> getPathSubstitutions(R resource) {
        return l -> {
            String uri = l.getUri();
            l.setUri(LinkUtils.replaceValues(uri, resource.getRequestAttributes()));
        };
    }
}
