package io.skysail.server.app.designer.fields;

import io.skysail.domain.html.InputType;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class DbEntityUrlField extends DbEntityField { // NOSONAR

    private static final long serialVersionUID = 4745549573124583164L;

    @Builder
    public DbEntityUrlField(@NonNull String name, boolean mandatory) {
        setType(InputType.URL);
        this.name = name;
        this.mandatory = mandatory;
    }
    
}
