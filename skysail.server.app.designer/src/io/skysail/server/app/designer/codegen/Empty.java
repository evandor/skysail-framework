package io.skysail.server.app.designer.codegen;

import io.skysail.api.domain.Identifiable;

public class Empty implements Identifiable {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
