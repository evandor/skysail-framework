package io.skysail.server.app.dbviewer.dbentities;

import io.skysail.server.model.DynamicBean;

import java.util.Map;

import lombok.*;

@SuppressWarnings("serial")
@Getter
@Setter
public class DbEntity extends DynamicBean {

    public DbEntity(String name, Map<String, Object> map) {
        super(name, map);
    }

}
