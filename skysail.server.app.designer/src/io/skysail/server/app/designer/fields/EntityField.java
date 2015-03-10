package io.skysail.server.app.designer.fields;

import lombok.Getter;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;

@Getter
@Setter
public class EntityField {

    @Field
    private String name;

    @Field
    private String type;

}
