package io.skysail.server.model;

import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class DefaultListFieldFactory extends FieldFactory {

    @Override
    public Map<String,FormField> determineFrom(SkysailServerResource<?> resource) {
        return ReflectionUtils.getInheritedFields(resource.getParameterizedType()).stream()
                .filter(f -> test(resource, f))
                .sorted((f1, f2) -> sort(resource, f1, f2))
                .map(f -> new FormField(f, resource))
                .collect(MyCollectors.toLinkedMap(FormField::getId, Function.identity()));

    }

    private int sort(SkysailServerResource<?> resource, Field f1, Field f2) {
        List<String> fieldNames = resource.getFields();
        return fieldNames.indexOf(f1.getName()) - fieldNames.indexOf(f2.getName());
    }
}
