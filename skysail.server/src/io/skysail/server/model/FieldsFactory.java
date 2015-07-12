package io.skysail.server.model;

import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

public class FieldsFactory {

    public static FieldFactory getFactory(SkysailResponse<?> response, SkysailServerResource<?> resource) {
        if (response.getEntity() == null) {
            return new NoFieldFactory();
        }
        if (response.getEntity() instanceof List) {
            return new DefaultListFieldFactory();
        } else if (response instanceof ConstraintViolationsResponse) {
            return entityFactory((ConstraintViolationsResponse<?>) response);
        } else if (response instanceof FormResponse) {
            return entityFactoryForForm((FormResponse<?>) response);
        } else {
            return new DefaultEntityFieldFactory(response.getEntity().getClass());
        }
    }

    private static FieldFactory entityFactory(ConstraintViolationsResponse<?> source) {
        Object entity = ((SkysailResponse<?>) source).getEntity();
        return new SkysailResponseEntityFieldFactory(source, entity.getClass());
    }

    private static FieldFactory entityFactoryForForm(FormResponse<?> source) {
        return new FormResponseEntityFieldFactory(source.getEntity().getClass());
    }

}
