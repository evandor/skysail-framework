package io.skysail.server.app.designer.fields;

import io.skysail.domain.html.InputType;
import lombok.*;

@NoArgsConstructor
public class DbEntityTrixeditorField extends DbEntityField { // NOSONAR

    private static final long serialVersionUID = 4745549573124583164L;

    @Builder
    public DbEntityTrixeditorField(@NonNull String name, boolean mandatory) {
        setType(InputType.TRIX_EDITOR);
        this.name = name;
        this.mandatory = mandatory;
    }

}
