package de.twenty11.skysail.server.services.test;

import de.twenty11.skysail.api.responses.LinkHeaderRelation;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class TestServerResource extends SkysailServerResource<String> {

    public TestServerResource() {
        super();
    }

    @Override
    public LinkHeaderRelation getLinkRelation() {
        return LinkHeaderRelation.ITEM;
    }

    @Override
    public String getEntity() {
        return null;
    }

}