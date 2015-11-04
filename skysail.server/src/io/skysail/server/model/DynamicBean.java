package io.skysail.server.model;

import io.skysail.api.domain.Identifiable;

import java.util.*;
import java.util.Map.Entry;

import lombok.Getter;

import org.apache.commons.beanutils.*;

public class DynamicBean extends BasicDynaClass implements Identifiable{

    private static final long serialVersionUID = -5013573289595878115L;

    @Getter
    private DynaBean bean;

    public DynamicBean(String name, Map<String, Object> map) {
        super(name, null, getProperties(map));
        try {
            bean = this.newInstance();
            map.entrySet().forEach(entry -> {
                bean.set(entry.getKey(), entry.getValue());
            });
        } catch (IllegalAccessException | InstantiationException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private static DynaProperty[] getProperties(Map<String, Object> map) {
        List<DynaProperty> props = new ArrayList<>();
        for (Entry<String, Object> entry : map.entrySet()) {
            props.add(new DynaProperty(entry.getKey(), entry.getValue().getClass()));
        }
        return props.toArray(new DynaProperty[props.size()]);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }

}
