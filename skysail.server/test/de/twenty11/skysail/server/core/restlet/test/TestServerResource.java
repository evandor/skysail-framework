package de.twenty11.skysail.server.core.restlet.test;

import io.skysail.api.links.LinkRelation;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class TestServerResource extends SkysailServerResource<String> {

    public TestServerResource() {
        super();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ITEM;
    }

    @Override
    public String getEntity() {
        return null;
    }

}