package io.skysail.server.converter.impl;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.converter.impl.factories.DefaultEntityFieldFactory;
import io.skysail.server.converter.impl.factories.DefaultListFieldFactory;
import io.skysail.server.converter.impl.factories.DynamicEntityFieldFactory;
import io.skysail.server.converter.impl.factories.ListEnumFieldFactory;
import io.skysail.server.converter.impl.factories.ListMapFieldFactory;
import io.skysail.server.converter.impl.factories.NoFieldFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.beans.DynamicEntity;

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
        return new NoFieldFactory();
    }

    private static FieldFactory entityFactory(Object source) {
        Object entity = ((SkysailResponse<?>) source).getEntity();
        if (entity instanceof DynamicEntity) {
            return new DynamicEntityFieldFactory((DynamicEntity) entity);
        } else {
            return new DefaultEntityFieldFactory((SkysailResponse<?>) source, entity.getClass());
        }
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
