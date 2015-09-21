package de.twenty11.skysail.server.app.tutorial.model2rest;

import io.skysail.api.domain.Identifiable;

public class Dummy implements Identifiable {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
