package io.skysail.server.converter.impl.factories;

import io.skysail.server.converter.impl.FieldFactory;
import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.server.core.FormField;

public class NoFieldFactory extends FieldFactory {

    @Override
    public List<FormField> determineFrom(SkysailServerResource<?> resource) {
        return Collections.emptyList();
    }

}