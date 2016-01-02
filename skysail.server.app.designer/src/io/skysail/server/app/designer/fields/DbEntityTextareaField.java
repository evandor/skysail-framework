package io.skysail.server.app.designer.fields;

import io.skysail.domain.html.*;
import io.skysail.server.forms.*;
import lombok.*;

@NoArgsConstructor
public class DbEntityTextareaField extends DbEntityField { // NOSONAR

    private static final long serialVersionUID = 4745549573124583164L;

    @Field(inputType = InputType.TEXT)
    @ListView(hide = true)
    @PostView(tab = "optional")
    private Integer width;
    
    @Field(inputType = InputType.TEXT)
    @ListView(hide = true)
    @PostView(tab = "optional")
    private Integer height;

    @Builder
    public DbEntityTextareaField(@NonNull String name, boolean mandatory) {
        setType(InputType.TEXTAREA);
        this.name = name;
        this.mandatory = mandatory;
    }
    
}
