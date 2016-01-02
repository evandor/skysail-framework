package io.skysail.server.app.designer.fields;

import io.skysail.domain.html.*;
import io.skysail.server.forms.*;
import lombok.*;

@NoArgsConstructor
public class DbEntityTextField extends DbEntityField { // NOSONAR

    private static final long serialVersionUID = 4745549573124583164L;

    @Field(inputType = InputType.TEXT)
    @ListView(hide = true)
    @PostView(tab = "optional")
    private Integer sizeMin;
    
    @Field(inputType = InputType.TEXT)
    @ListView(hide = true)
    @PostView(tab = "optional")
    private Integer sizeMax;

    @Builder
    public DbEntityTextField(@NonNull String name, boolean mandatory) {
        setType(InputType.TEXT);
        this.name = name;
        this.mandatory = mandatory;
    }

}
