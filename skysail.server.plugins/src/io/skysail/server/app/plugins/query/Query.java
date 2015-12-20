package io.skysail.server.app.plugins.query;

import io.skysail.api.forms.Field;
import io.skysail.domain.Identifiable;
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
