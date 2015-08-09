package io.skysail.server.documentation;

import io.skysail.server.forms.FormField;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.*;
import java.util.stream.Collectors;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class EntityResource extends ListServerResource<FieldDescriptor> {

    @Override
    protected void doInit() {
        super.doInit();
        addToContext(ResourceContextId.LINK_TITLE, "show Entity");
    }

    @Override
    public List<FieldDescriptor> getEntity() {
        String className = getAttribute("id");

        try {
            Map<String, FormField> description = getApplication().describe(className);
            return description.values().stream().map(ff -> new FieldDescriptor(ff)).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


}
