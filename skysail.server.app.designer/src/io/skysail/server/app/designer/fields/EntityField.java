package io.skysail.server.app.designer.fields;

import io.skysail.api.forms.*;
import io.skysail.server.app.designer.fields.resources.InputTypeSelectionProvider;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
public class EntityField {

    @Id
    private String id;

    @Field
    private String name;

    @Field(selectionProvider = InputTypeSelectionProvider.class)
    private InputType type;
}
