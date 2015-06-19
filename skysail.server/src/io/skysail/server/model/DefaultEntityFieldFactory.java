package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import lombok.NonNull;
import de.twenty11.skysail.server.core.FormField;

public class DefaultEntityFieldFactory extends FieldFactory {

    private Object source;

    public DefaultEntityFieldFactory(@NonNull Object source) {
        this.source = source;
    }

    @Override
    public Map<String,FormField> determineFrom(SkysailServerResource<?> resource, List<Map<String, Object>> data) throws Exception {
        Class<?> cls = source.getClass();
        List<Field> inheritedFields = ReflectionUtils.getInheritedFields(cls);
        System.out.println(inheritedFields);
        return ReflectionUtils.getInheritedFields(cls).stream()
                .filter(f -> test(resource, f))
                .map(f -> new FormField(f, resource))
                .collect(MyCollectors.toLinkedMap(FormField::getName, Function.identity()));
    }
}
