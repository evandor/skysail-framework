package io.skysail.server.app.designer.fields.resources;

import java.util.*;

import io.skysail.domain.html.InputType;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.resources.date.PutDateFieldResource;
import io.skysail.server.app.designer.fields.resources.editors.PutTrixeditorFieldResource;
import io.skysail.server.app.designer.fields.resources.text.PutTextFieldResource;
import io.skysail.server.app.designer.fields.resources.textarea.*;
import io.skysail.server.restlet.resources.*;

public class PutFieldRedirectResource extends RedirectResource<DbEntityField> {

    private static Map<String, Class<? extends SkysailServerResource<?>>> clsMap = new HashMap<>();

    static {
        addToClassMap(InputType.DATE, PutDateFieldResource.class);
        addToClassMap(InputType.TEXT, PutTextFieldResource.class);
        addToClassMap(InputType.TEXTAREA, PutTextareaFieldResource.class);
        addToClassMap(InputType.TRIX_EDITOR, PutTrixeditorFieldResource.class);
    }

    @Override
    protected Class<? extends SkysailServerResource<?>> redirectToResource() {
        String classHint = getQuery().getFirstValue("classHint");
        Class<? extends SkysailServerResource<?>> result = clsMap.get(classHint);
        if (result == null) {
            throw new IllegalStateException("could not map classHint '" + classHint + "'");
        }
        return result;
    }

    private static void addToClassMap(InputType inputType, Class<? extends SkysailServerResource<?>> cls) {
        clsMap.put(inputType.name(), cls);
    }
}
