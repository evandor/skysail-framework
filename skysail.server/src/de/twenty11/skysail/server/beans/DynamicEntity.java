package de.twenty11.skysail.server.beans;

import java.util.Collections;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaBean;

@Slf4j
public class DynamicEntity implements DynamicBean {

    private DynaBean instance;

    private String beanName;

    public DynamicEntity(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public synchronized DynaBean getInstance() {
        if (instance == null) {
            try {
                instance = new EntityDynaClass(beanName, getProperties()).newInstance();
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
