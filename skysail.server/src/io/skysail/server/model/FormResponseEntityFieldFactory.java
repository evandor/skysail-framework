package io.skysail.server.model;

import io.skysail.api.responses.FormResponse;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ReflectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import lombok.NonNull;
import de.twenty11.skysail.server.core.FormField;

public class FormResponseEntityFieldFactory extends FieldFactory {

    private Class<? extends Object> cls;
    private FormResponse<?> source;
    private Object entity;

    public FormResponseEntityFieldFactory(@NonNull FormResponse<?> source, Object entity) {
        this.source = source;
        this.entity = entity;
        this.cls = entity.getClass();
    }

    /**
     * the class object which was provided in the constructor is scanned for its fields and
     * for each of those which are valid in respect to the current resource (see the 
     * "test" method) a new FormField is created. 
     */
    @Override
    public List<FormField> determineFrom(SkysailServerResource<?> resource) throws Exception {
        //resource.setCurrentEntity(entity);
        return ReflectionUtils.getInheritedFields(cls).stream()
                .filter(f -> test(resource, f))
                .map(f -> new FormField(f, resource, source))//, source.getEntity()))
                .collect(Collectors.toList());
    }

}
