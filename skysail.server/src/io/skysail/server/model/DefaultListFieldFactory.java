package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import de.twenty11.skysail.server.core.FormField;

public class DefaultListFieldFactory extends FieldFactory {

    private List<?> source;

    public DefaultListFieldFactory(Object source) {
        this.source = (List<?>)source;
    }

    @Override
    public List<FormField> determineFrom(SkysailServerResource<?> resource, List<Map<String, Object>> data) throws Exception {
        return ReflectionUtils.getInheritedFields(resource.getParameterizedType()).stream()
                .filter(f -> test(resource, f))
                .sorted((f1, f2) -> sort(resource, f1, f2))
                .map(f -> new FormField(f, resource, (List<?>)source))//
                .collect(Collectors.toList());
    }

    private int sort(SkysailServerResource<?> resource, Field f1, Field f2) {
        List<String> fieldNames = resource.getFields();
        return fieldNames.indexOf(f1.getName()) - fieldNames.indexOf(f2.getName());
    }
}
