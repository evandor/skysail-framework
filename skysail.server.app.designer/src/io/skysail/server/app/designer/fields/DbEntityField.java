package io.skysail.server.app.designer.fields;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import io.skysail.api.forms.InputType;
import io.skysail.server.app.designer.fields.resources.InputTypeSelectionProvider;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DbEntityField implements Identifiable {

    @Id
    private String id;

    @Field
    @NotNull
    @Size(min = 1)
    private String name;

    @Field(selectionProvider = InputTypeSelectionProvider.class)
    private InputType type;
    
    @Field(inputType = InputType.CHECKBOX)
    private Boolean notNull;
    
    @Field(inputType = InputType.TEXT)
    private Integer sizeMin;
    
    @Field(inputType = InputType.TEXT)
    private Integer sizeMax;

}
