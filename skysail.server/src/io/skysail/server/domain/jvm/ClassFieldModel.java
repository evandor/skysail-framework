package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;

import javax.validation.constraints.*;

import io.skysail.api.forms.InputType;
import io.skysail.server.forms.*;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper = true)
public class ClassFieldModel extends io.skysail.domain.core.FieldModel {

    @Getter
    private Class<? extends SkysailServerResource<?>> listViewLink;

    private Field f;

    public ClassFieldModel(String id) {
        super(id);
        f = null;
        listViewLink = null;
    }

    public ClassFieldModel(java.lang.reflect.Field f) {
        super(f.getName());
        this.f = f;
        setInputType(determineInputType(f));
        setMandatory(determineIfMandatory(f));
        setReadonly(false);
        setTruncateTo(determineTruncation(f));
        setType(f.getType());

        listViewLink = determineListViewLink(f);
    }
    
    public String getPostTabName() {
        PostView postAnnotation = f.getAnnotation(PostView.class);
        return postAnnotation == null ? null : postAnnotation.tab(); 
    }
    
    private Class<? extends SkysailServerResource<?>> determineListViewLink(Field f) {
        ListView listViewAnnotation = f.getAnnotation(ListView.class);
        if (listViewAnnotation != null && !listViewAnnotation.link().equals(ListView.DEFAULT.class)) {
            return listViewAnnotation.link();
        }
        return null;
    }

    private InputType determineInputType(Field f) {
        return f.getAnnotation(io.skysail.api.forms.Field.class).inputType();
    }

    private boolean determineIfMandatory(Field f) {
        NotNull notNullAnnotation = f.getAnnotation(NotNull.class);
        if (notNullAnnotation != null) {
            return true;
        }
        Size sizeAnnotation = f.getAnnotation(Size.class);
        if (sizeAnnotation != null) {
            int min = sizeAnnotation.min();
            if (min > 0) {
                return true;
            }
        }
        return false;
    }

    private Integer determineTruncation(Field f) {
        ListView listViewAnnotation = f.getAnnotation(ListView.class);
        if (listViewAnnotation != null && !listViewAnnotation.link().equals(ListView.DEFAULT.class)) {
            return null;
        }
        return null;
    }

    
}
