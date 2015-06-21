package io.skysail.server.app.designer.fields;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.designer.fields.resources.InputTypeSelectionProvider;

import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@ToString
public class EntityField implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;

    @Field(selectionProvider = InputTypeSelectionProvider.class)
    private InputType type;
    
    @Field(type = InputType.CHECKBOX)
    private Boolean notNull;
    
    @Field(type = InputType.TEXT)
    private Integer sizeMin;
    
    @Field(type = InputType.TEXT)
    private Integer sizeMax;

}
