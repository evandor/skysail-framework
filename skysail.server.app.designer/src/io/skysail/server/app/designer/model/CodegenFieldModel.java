package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.domain.core.FieldModel;
import lombok.*;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class CodegenFieldModel extends FieldModel {

    private final String name;

    public CodegenFieldModel(DbEntityField f) {
        super(f.getName());
        this.name = f.getName();
        setInputType(f.getType());
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
