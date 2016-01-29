package io.skysail.server.app.designer.fields;

import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.forms.PostView;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class DbEntityDateField extends DbEntityField { // NOSONAR

    private static final long serialVersionUID = 4745549573124583164L;

    @Field
    @PostView(tab = "special")
    protected boolean modifiedAt;

    @Builder
    public DbEntityDateField(@NonNull String name, boolean mandatory) {
        setType(InputType.DATE);
        this.name = name;
        this.mandatory = mandatory;
    }

}
