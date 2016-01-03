package io.skysail.server.app.designer.fields;

import java.io.Serializable;

import javax.persistence.Id;
import javax.validation.constraints.*;

import io.skysail.domain.*;
import io.skysail.domain.html.*;
import io.skysail.server.app.designer.fields.resources.InputTypeSelectionProvider;
import io.skysail.server.forms.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class DbEntityField implements Identifiable, Nameable, Serializable {

    private static final long serialVersionUID = -3876765006276811418L;

    @Id
    @Setter
    protected String id;

    @Field
    @NotNull
    @Size(min = 1)
    @PostView(tab = "new Field")
    //@ListView(link = PutFieldResource.class)
    protected String name;

    @Field(selectionProvider = InputTypeSelectionProvider.class)
    @PostView(visibility = Visibility.HIDE)
    private InputType type;
    
    @Field(inputType = InputType.CHECKBOX)
    @PostView(tab = "optional")
    protected Boolean mandatory;
    

}
