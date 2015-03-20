package de.twenty11.skysail.server.beans;

import io.skysail.api.forms.Field;
import lombok.Getter;

import org.apache.commons.beanutils.DynaProperty;

public class EntityDynaProperty extends DynaProperty implements Comparable<EntityDynaProperty> {

    private static final long serialVersionUID = 7455333768454805831L;

    @Getter
    private Field field;

    public EntityDynaProperty(String name) {
        super(name);
    }

    public EntityDynaProperty(String propertyName, Class<String> class1) {
        super(propertyName, class1);
    }

    @Override
    public int compareTo(EntityDynaProperty arg0) {
        return 1;
    }

}
