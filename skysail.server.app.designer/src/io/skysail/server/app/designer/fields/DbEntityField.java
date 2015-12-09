package io.skysail.server.app.designer.fields;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.*;
import io.skysail.server.app.designer.fields.resources.InputTypeSelectionProvider;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
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
