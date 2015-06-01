package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import de.twenty11.skysail.server.core.FormField;

public class ListEnumFieldFactory extends FieldFactory {

    @Override
    public List<FormField> determineFrom(SkysailServerResource<?> resource) {
        return Arrays.asList(new FormField(resource.getParameterType().getSimpleName(), "hi"));
    }

}
