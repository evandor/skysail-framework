package io.skysail.server.documentation;

import io.skysail.api.forms.Field;
import io.skysail.server.forms.FormField;
import lombok.Getter;

@Getter
public class FieldDescriptor {

    @Field
    private Class<?> type;

    @Field
    private String inputType;

    private String classname;


    public FieldDescriptor(FormField ff) {
        this.classname = ff.getName();
        this.type = ff.getType();
        this.inputType = ff.getInputType();
    }
}
