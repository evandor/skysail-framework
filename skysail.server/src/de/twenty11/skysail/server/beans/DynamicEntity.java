package de.twenty11.skysail.server.beans;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.beanutils.DynaBean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Slf4j
@JsonIgnoreProperties("handler")
public class DynamicEntity implements DynamicBean {

    private DynaBean instance;

    @Override
    public String getBeanName() {
         String simpleName = this.getClass().getSimpleName();
         String[] split = simpleName.split("\\_\\$\\$\\_");
        if (split.length > 1) {
            return split[0];
        }
        return simpleName;
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
