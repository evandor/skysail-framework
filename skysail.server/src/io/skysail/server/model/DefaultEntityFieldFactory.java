package io.skysail.server.model;

import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.*;

import java.util.Map;
import java.util.function.Function;

import lombok.NonNull;

public class DefaultEntityFieldFactory extends FieldFactory {

    private Class<?> cls;

    public DefaultEntityFieldFactory(@NonNull Class<?> cls) {
        this.cls = cls;
    }

    @Override
    public Map<String,FormField> determineFrom(SkysailServerResource<?> resource) {
        return ReflectionUtils.getInheritedFields(cls).stream()
                .filter(f -> test(resource, f))
                .map(f -> new FormField(f, resource))
                .collect(MyCollectors.toLinkedMap(FormField::getName, Function.identity()));
    }
}
