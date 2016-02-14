package io.skysail.server.stringtemplate.test;

import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class ATestResource extends ListServerResource<AnEntity> {

    @Override
    public List<?> getEntity() {
        return null;
    }

}
