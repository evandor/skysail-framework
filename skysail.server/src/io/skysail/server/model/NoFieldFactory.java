package io.skysail.server.model;

import io.skysail.server.restlet.resources.SkysailServerResource;

import java.util.*;

import de.twenty11.skysail.server.core.FormField;

public class NoFieldFactory extends FieldFactory {

    @Override
    public List<FormField> determineFrom(SkysailServerResource<?> resource, List<Map<String, Object>> data) {
        return Collections.emptyList();
    }

}
