package de.twenty11.skysail.server.validation.test;

import java.io.Serializable;

import org.junit.Ignore;

import de.twenty11.skysail.api.domain.Identifiable;

@Ignore
public class Entity implements Identifiable, Serializable {

    private static final long serialVersionUID = 917647823437327405L;

    private String id;

    private String name;

    public Entity(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

}
