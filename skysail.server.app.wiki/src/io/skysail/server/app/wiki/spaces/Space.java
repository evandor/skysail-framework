package io.skysail.server.app.wiki.spaces;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;

public class Space extends DynamicEntity {

    public Space() {
        super("Contract", getProperties());
    }

    private static Set<EntityDynaProperty> getProperties() {
        SortedSet<EntityDynaProperty> properties = new TreeSet<>();
        properties.add(new EntityDynaProperty("name", String.class));
        return properties;
    }

}
