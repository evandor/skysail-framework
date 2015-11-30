package io.skysail.server.domain.jvm;

import io.skysail.api.forms.InputType;
import io.skysail.server.forms.ListView;

import java.lang.reflect.Field;

import javax.validation.constraints.*;

import lombok.*;

@Value
@EqualsAndHashCode(callSuper=true)
public class ClassField extends io.skysail.server.domain.core.FieldModel {

    public ClassField(String id) {
        super(id);
    }

    public ClassField(java.lang.reflect.Field f) {
        super(f.getName());
        setInputType(determineInputType(f));
        setMandatory(determineIfMandatory(f));
        setReadonly(false);
        setTruncateTo(determineTruncation(f));
        setType(f.getType());
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
