package io.skysail.server.app.crm.contracts;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import de.twenty11.skysail.server.beans.DynamicEntity;
import de.twenty11.skysail.server.beans.EntityDynaProperty;

public class Contract extends DynamicEntity {

    public Contract() {
        super("Contract");
    }

    public Set<EntityDynaProperty> getProperties() {
        SortedSet<EntityDynaProperty> properties = new TreeSet<>();
        properties.add(new EntityDynaProperty("name", String.class));
        return properties;
    }

}
