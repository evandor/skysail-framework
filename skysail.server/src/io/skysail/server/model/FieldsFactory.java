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
            return listFactory((List<?>)source, resource);
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

    private static FieldFactory listFactory(List<?> source, SkysailServerResource<?> resource) {
        Class<?> parameterType = resource.getParameterizedType();
        if (parameterType.equals(Map.class)) {
            //return new ListMapFieldFactory();
            throw new UnsupportedOperationException();
        } else if (parameterType.isEnum()) {
           // return new ListEnumFieldFactory();
            throw new UnsupportedOperationException();
        } else {
            return new DefaultListFieldFactory(source);
        }
    }

}
