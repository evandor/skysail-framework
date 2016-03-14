package io.skysail.server.app.designer.valueobjects;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValueObject implements Identifiable {

    private String id;
    
    @Field
    private String name;
}
