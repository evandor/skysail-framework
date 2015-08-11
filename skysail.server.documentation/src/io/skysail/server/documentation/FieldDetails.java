package io.skysail.server.documentation;

import io.skysail.server.forms.FormField;
import lombok.Getter;

@Getter
public class FieldDetails {

    private Class<?> type;

    private String inputType;

    public FieldDetails(FormField ff) {
        type = ff.getType();
        inputType = ff.getInputType();
    }

}
