package io.skysail.server.app.designer.fields;

import io.skysail.api.forms.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityField {

    @Field
    private String name;

    @Field
    private String type;

}
