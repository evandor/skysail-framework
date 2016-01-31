package io.skysail.server.app.designer.fields;

import java.util.Date;

import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.server.app.designer.fields.roles.FieldRoleSelectionProvider;
import io.skysail.server.forms.PostView;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class DbEntityDateField extends DbEntityField { // NOSONAR

    private static final long serialVersionUID = 4745549573124583164L;

    @Field(selectionProvider = FieldRoleSelectionProvider.class)
    @PostView(tab = "special")
    protected FieldRole role;

    @Builder
    public DbEntityDateField(@NonNull String name, boolean mandatory) {
        setType(InputType.DATE);
        this.name = name;
        this.mandatory = mandatory;
    }
    
    @Override
    public Class<?> getFieldType() {
        return Date.class;
    }

}
