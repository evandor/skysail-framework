package io.skysail.server.app.designer.fields;

import java.util.Date;

import io.skysail.domain.html.InputType;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class DbEntityDateField extends DbEntityField { // NOSONAR

    private static final long serialVersionUID = 4745549573124583164L;

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
