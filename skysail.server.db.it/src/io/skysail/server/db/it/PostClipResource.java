package io.skysail.server.db.it;

import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostClipResource extends PostEntityServerResource<TestEntity> {

    @Override
    public TestEntity createEntityTemplate() {
        return new TestEntity();
    }

}
