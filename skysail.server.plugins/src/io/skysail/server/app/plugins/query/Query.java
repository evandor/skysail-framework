package io.skysail.server.app.plugins.query;

import io.skysail.api.domain.Identifiable;
import io.skysail.api.forms.Field;
import lombok.Data;

@Data
public class Query implements Identifiable {

    @Field
    private String searchTerm;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }
}
