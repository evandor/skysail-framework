package io.skysail.server.model;

import io.skysail.api.forms.Postfix;
import io.skysail.api.forms.Prefix;
import io.skysail.server.forms.ListView;

import java.lang.reflect.Field;

public class FieldDescriptor {

    private io.skysail.api.forms.Field formField;
    private ListView listView;
    private Postfix postfix;
    private Prefix prefix;
    
    private Class<?> type;

    public FieldDescriptor(Field field) {
        formField = field.getAnnotation(io.skysail.api.forms.Field.class);
        listView = field.getAnnotation(ListView.class);
        postfix = field.getAnnotation(Postfix.class);
        prefix = field.getAnnotation(Prefix.class);
        type = field.getType();
    }

}
