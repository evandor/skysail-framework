package io.skysail.server.app.designer.model;

import io.skysail.domain.core.FieldModel;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.designer.fields.DbEntityField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "name")
@ToString
public class DesignerFieldModel extends FieldModel {

    private final String name;

    public DesignerFieldModel(DbEntityField f) {
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
    
    public String getHtmlPolicy() {
        if (inputType.equals(InputType.TRIX_EDITOR)) {
            return HtmlPolicy.TRIX_EDITOR.name();
        }
        return HtmlPolicy.NO_HTML.name();
    }

    private static String capitalized(String string) {
        return string.substring(0,1).toUpperCase().concat(string.substring(1));
    }


}
