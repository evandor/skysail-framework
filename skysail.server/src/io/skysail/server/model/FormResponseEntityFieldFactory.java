package io.skysail.server.model;

import io.skysail.api.responses.FormResponse;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.*;

import java.util.*;
import java.util.function.Function;

import lombok.NonNull;
import de.twenty11.skysail.server.core.FormField;

public class FormResponseEntityFieldFactory extends FieldFactory {

    private Class<? extends Object> cls;
    private FormResponse<?> source;
    private Object entity;

    public FormResponseEntityFieldFactory(@NonNull FormResponse<?> source, Object entity) {
        this.source = source;
        this.entity = entity;
        this.cls = entity != null ? entity.getClass() : null;
    }

    /**
     * the class object which was provided in the constructor is scanned for its fields and
     * for each of those which are valid in respect to the current resource (see the 
     * "test" method) a new FormField is created. 
     */
    @Override
    public Map<String,FormField> determineFrom(SkysailServerResource<?> resource, List<Map<String, Object>> data) throws Exception {
        return ReflectionUtils.getInheritedFields(cls).stream()
                .filter(f -> test(resource, f))
                .map(f -> new FormField(f, resource, source))//, source.getEntity()))
                .collect(MyCollectors.toLinkedMap(FormField::getName, Function.identity()));
    }

}
