package io.skysail.server.app.designer.model;

import io.skysail.server.app.designer.fields.ActionEntityField;
import io.skysail.server.app.designer.fields.ActionType;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class ActionFieldModel {

    private final String name;
    
    private Map<String,String> codes = new HashMap<>();

    private ActionType actionType;

    public ActionFieldModel(@NonNull ActionEntityField f) {
        this.name = f.getName();
        this.codes = f.getCodes();
        this.actionType = f.getType();
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

    public String getCode(String string) {
        return codes.get(string);
    }
    
    public String getType() {
        return actionType.getType().getSimpleName();
    }


}
