package io.skysail.server.model;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

public class FieldsFactory {

    public static FieldFactory getFactory(Object source, SkysailServerResource<?> resource) {
        if (source == null) {
            return new NoFieldFactory();
        }
        if (source instanceof List) {
            return listFactory(source, resource);
        }
        if (source instanceof SkysailResponse) {
            return entityFactory(source);
        }
        return new DefaultEntityFieldFactory(source);
    }

    private static FieldFactory entityFactory(Object source) {
        Object entity = ((SkysailResponse<?>) source).getEntity();
        return new SkysailResponseEntityFieldFactory((SkysailResponse<?>) source, entity.getClass());
    }

    private static FieldFactory listFactory(Object source, SkysailServerResource<?> resource) {
        Class<?> parameterType = resource.getParameterType();
        if (parameterType.equals(Map.class)) {
            return new ListMapFieldFactory();
        } else if (parameterType.isEnum()) {
            return new ListEnumFieldFactory();
        } else {
            return new DefaultListFieldFactory(source);
        }
    }

}
