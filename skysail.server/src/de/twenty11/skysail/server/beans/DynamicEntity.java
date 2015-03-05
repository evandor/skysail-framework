package de.twenty11.skysail.server.beans;

import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaBean;

@Slf4j
public class DynamicEntity implements DynamicBean {

    private EntityDynaClass dynaClass;

    private DynaBean instance;

    public DynamicEntity(String beanName, Set<EntityDynaProperty> properties) {
        dynaClass = new EntityDynaClass(beanName, properties);
        try {
            instance = dynaClass.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public DynaBean getInstance() {
        return instance;
    }

    public String getString(String ident) {
        return (String) instance.get(ident);
    }

}
