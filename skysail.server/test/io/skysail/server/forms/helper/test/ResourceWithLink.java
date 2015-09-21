package io.skysail.server.forms.helper.test;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.links.Link;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;

public class ResourceWithLink extends ListServerResource<Identifiable> {

    @Override
    public List<String> getEntity() {
        return Arrays.asList("hi");
    }

    @Override
    public List<Link> getLinks() {
        List<Link> links = new ArrayList<>();
        links.add(new Link.Builder("uri").build());
        return links;
    }

}
