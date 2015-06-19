package io.skysail.server.model;

import io.skysail.api.forms.Reference;
import io.skysail.server.forms.*;
import io.skysail.server.restlet.resources.*;

import java.lang.reflect.Field;
import java.util.*;

import de.twenty11.skysail.server.core.FormField;

public abstract class FieldFactory {

    public abstract List<FormField> determineFrom(SkysailServerResource<?> resource, List<Map<String, Object>> data) throws Exception;
    
    protected boolean test(SkysailServerResource<?> resource, Field field) {
        List<String> fieldNames = resource.getFields();
        if (isValidFieldAnnotation(resource, field, fieldNames)) {
            return true;
        }
        return false;
    }

    private boolean isValidFieldAnnotation(SkysailServerResource<?> resource, Field field, List<String> fieldNames) {
        io.skysail.api.forms.Field fieldAnnotation = field.getAnnotation(io.skysail.api.forms.Field.class);
        Reference referenceAnnotation = field.getAnnotation(Reference.class);
        if (fieldAnnotation == null && referenceAnnotation == null) {
            return false;
        }
        if (!(fieldNames.contains(field.getName()))) {
            return false;
        }
        if (resource instanceof PostEntityServerResource<?>) {
            PostView postViewAnnotation = field.getAnnotation(PostView.class);
            if (postViewAnnotation != null) {
                if (Visibility.HIDE.equals((postViewAnnotation.visibility()))) {
                    return false;
                }
                if (Visibility.SHOW.equals(postViewAnnotation.visibility())) {
                    return true;
                }
                if (Visibility.SHOW_IF_NULL.equals(postViewAnnotation.visibility())) {
                    if (resource.getRequest().toString().contains("/" + field.getName() + ":null/")) {
                        return true;
                    }
                }
            }
        }
        ListView listViewAnnotation = field.getAnnotation(ListView.class);
        if (listViewAnnotation == null) {
            return true;
        }
        return !listViewAnnotation.hide();
    }

}
