package io.skysail.server.app.plugins.query;

import io.skysail.api.forms.Field;
import lombok.Data;

@Data
public class Query {

    @Field
    private String searchTerm;
}
