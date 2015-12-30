package io.skysail.server.app.plugins.query;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
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
