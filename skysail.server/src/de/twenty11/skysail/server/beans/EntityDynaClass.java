package de.twenty11.skysail.server.beans;

import java.util.Set;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;

public class EntityDynaClass extends BasicDynaClass {

    private static final long serialVersionUID = 9205578348141992509L;
    private Set<EntityDynaProperty> properties;

    public EntityDynaClass(String beanName, Set<EntityDynaProperty> properties) {
        super(beanName, null, properties.toArray(new DynaProperty[properties.size()]));
        this.properties = properties;

    }

}
