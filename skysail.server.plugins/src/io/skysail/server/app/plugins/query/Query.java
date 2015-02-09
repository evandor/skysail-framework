package io.skysail.server.app.plugins.query;

import lombok.Data;
import de.twenty11.skysail.api.forms.Field;

@Data
public class Query {

    @Field
    private String searchTerm;
}
