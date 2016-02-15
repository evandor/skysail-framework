package io.skysail.product.notes;

import java.util.*;

import io.skysail.server.restlet.resources.ListServerResource;

public class PublicResource extends ListServerResource<Empty> {

    @Override
    public List<?> getEntity() {
        return Collections.emptyList();
    }

}
