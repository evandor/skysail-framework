package io.skysail.server.ext.oauth2.impl;

import io.skysail.domain.Identifiable;

public class Empty implements Identifiable {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
