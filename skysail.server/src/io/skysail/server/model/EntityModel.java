package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.Map;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import de.twenty11.skysail.server.core.FormField;

@ToString
@Slf4j
public class EntityModel<R extends SkysailServerResource<?>> {

    private Object entity;
    private R resource;
    
    private Map<String, FormField> fields;

    public EntityModel(Object entity, R resource) {
        this.entity = entity;
        this.resource = resource;
        determineFormfields();
    }

    private void determineFormfields() {
        FieldFactory fieldFactory = FieldsFactory.getFactory(entity, resource);
        try {
            fields = fieldFactory.determineFrom(resource);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
