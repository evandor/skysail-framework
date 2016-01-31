package io.skysail.server.app.designer.model;

import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import io.skysail.domain.html.HtmlPolicy;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.designer.fields.DbEntityDateField;
import io.skysail.server.app.designer.fields.DbEntityField;
import io.skysail.server.app.designer.fields.FieldRole;
import io.skysail.server.forms.Visibility;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(of = "name")
@ToString(of = {"name","role", "listViewVisibility", "postViewVisibility", "putViewVisibility"})
public class DesignerFieldModel extends FieldModel {

    private final String name;
    private final Visibility listViewVisibility;
    private final Visibility postViewVisibility;
    private final Visibility putViewVisibility;
    private FieldRole role;

    private DesignerEntityModel entityModel;

    public DesignerFieldModel(DesignerEntityModel entityModel, DbEntityField dbField) {
        super(dbField.getName(), dbField.getFieldType());
        this.entityModel = entityModel;
        this.name = dbField.getName();
        setInputType(dbField.getType());
        listViewVisibility = Visibility.valueOf(dbField.getListViewVisibility());
        postViewVisibility = Visibility.valueOf(dbField.getPostViewVisibility());
        putViewVisibility = Visibility.valueOf(dbField.getPutViewVisibility());
        if (dbField instanceof DbEntityDateField) {
            this.role = ((DbEntityDateField)dbField).getRole();
        }
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

    public String getListViewAnnotation() {
        if (!entityModel.getRelations().isEmpty()) {
            EntityModel targetEntityModel = entityModel.getRelations().get(0).getTargetEntityModel();
            String target = targetEntityModel.getSimpleName() +"sResource.class";
            return new StringBuilder("@ListView(link = ").append(target).append(")").toString();
        }
        if (listViewVisibility.equals(Visibility.SHOW)) {
            return null;
        }
        return new StringBuilder("@ListView(hide = true)").toString();
    }

    public String getPostViewAnnotation() {
        if (postViewVisibility.equals(Visibility.SHOW)) {
            return null;
        }
        return new StringBuilder("@PostView(visibility = Visibility.").append(postViewVisibility.name()).append(")").toString();
    }

    public String getPutViewAnnotation() {
        if (putViewVisibility.equals(Visibility.SHOW)) {
            return null;
        }
        return new StringBuilder("@PutView(visibility = Visibility.").append(putViewVisibility.name()).append(")").toString();
    }

    private static String capitalized(String string) {
        return string.substring(0,1).toUpperCase().concat(string.substring(1));
    }


}
