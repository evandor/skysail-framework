package io.skysail.server.app.designer.entities;

import lombok.Getter;
import lombok.Setter;
import de.twenty11.skysail.api.forms.Field;

@Getter
@Setter
public class Entity {

    @Field
    private String name;

}
