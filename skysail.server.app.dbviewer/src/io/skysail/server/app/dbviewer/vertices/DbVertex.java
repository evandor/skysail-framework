package io.skysail.server.app.dbviewer.vertices;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import lombok.*;

@Getter
@Setter
public class DbVertex implements Identifiable {

    private String id;

    @Field
    private String name;
}
