package io.skysail.server.converter.impl.factories;

import io.skysail.server.converter.impl.FieldFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.Arrays;
import java.util.List;

import de.twenty11.skysail.server.core.FormField;

public class ListEnumFieldFactory extends FieldFactory {

    @Override
    public List<FormField> determineFrom(SkysailServerResource<?> resource) {
        return Arrays.asList(new FormField(resource.getParameterType().getSimpleName(), "hi"));
    }

}
