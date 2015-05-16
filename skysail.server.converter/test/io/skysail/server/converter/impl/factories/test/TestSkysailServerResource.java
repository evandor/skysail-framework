package io.skysail.server.converter.impl.factories.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.server.restlet.resources.SkysailServerResource;

class TestSkysailServerResource extends SkysailServerResource<TestEntity> {
    @Override
    public TestEntity getEntity() {
        return null;
    }
    @Override
    public LinkRelation getLinkRelation() {
        return null;
    }
}