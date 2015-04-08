package de.twenty11.skysail.server.beans;

import java.util.Collections;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaBean;

@Slf4j
public class DynamicEntity implements DynamicBean {

    private DynaBean instance;

    @Override
    public String getBeanName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public synchronized DynaBean getInstance() {
        if (instance == null) {
            try {
                instance = new EntityDynaClass(getBeanName(), getProperties()).newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return instance;
    }

    public String getString(String ident) {
        return (String) getInstance().get(ident);
    }

    public Set<EntityDynaProperty> getProperties() {
        return Collections.emptySet();
    }
   
}
