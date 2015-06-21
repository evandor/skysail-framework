package io.skysail.server.model;

import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;

public class FieldsFactory {

    public static FieldFactory getFactory(Object source, SkysailServerResource<?> resource) {
        if (source == null) {
            return new NoFieldFactory();
        }
        if (source instanceof List) {
            return new DefaultListFieldFactory();
        } else if (source instanceof ConstraintViolationsResponse) {
            return entityFactory((ConstraintViolationsResponse<?>) source);
        } else if (source instanceof FormResponse) {
            return entityFactoryForForm((FormResponse<?>) source);
        } else {
            return new DefaultEntityFieldFactory(source.getClass());
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
