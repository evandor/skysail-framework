package io.skysail.server.model;

import io.skysail.api.responses.*;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

public class FieldsFactory {

    public static FieldFactory getFactory(Object source, SkysailServerResource<?> resource) {
        if (source == null) {
            return new NoFieldFactory();
        }
        if (source instanceof List) {
            return listFactory((List<?>) source, resource);
        } else if (source instanceof ConstraintViolationsResponse) {
            return entityFactory((ConstraintViolationsResponse<?>)source);
        } else if (source instanceof FormResponse) {
            return entityFactoryForForm((FormResponse<?>)source);
        } else {
            return new DefaultEntityFieldFactory(source);
        }
    }

    private static FieldFactory entityFactory(ConstraintViolationsResponse<?> source) {
        Object entity = ((SkysailResponse<?>) source).getEntity();
        return new SkysailResponseEntityFieldFactory(source, entity.getClass());
    }
    
    private static FieldFactory entityFactoryForForm(FormResponse<?> source) {
        return new FormResponseEntityFieldFactory(source, source.getEntity());
    }

    private static FieldFactory listFactory(List<?> source, SkysailServerResource<?> resource) {
        Class<?> parameterType = resource.getParameterizedType();
        if (parameterType.equals(Map.class)) {
            // return new ListMapFieldFactory();
            throw new UnsupportedOperationException();
        } else if (parameterType.isEnum()) {
            // return new ListEnumFieldFactory();
            throw new UnsupportedOperationException();
        } else {
            return new DefaultListFieldFactory(source);
        }
    }

}
