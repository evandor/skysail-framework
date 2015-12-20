package io.skysail.server.app.designer.fields;

import java.io.Serializable;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.api.forms.*;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.designer.fields.resources.InputTypeSelectionProvider;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class DbEntityField implements Identifiable, Serializable {

    private static final long serialVersionUID = -3876765006276811418L;

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

    @Builder
    public DbEntityField(@NonNull String name, @NonNull InputType type, boolean notNull) {
        this.type = type;
        this.name = name;
        this.notNull = notNull;
    }

}
