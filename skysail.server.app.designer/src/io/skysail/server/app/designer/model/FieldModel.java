package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.fields.EntityField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class FieldModel {

    private final String name;

    public FieldModel(@NonNull EntityField f) {
        this.name = f.getName();
    }
    
    public String getGetMethodName() {
        return "get".concat(capitalized(name));
    }

    public String getSetMethodName() {
        return "set".concat(capitalized(name));
    }

    private String capitalized(String string) {
        return string.substring(0,1).toUpperCase().concat(string.substring(1));
    }


}
