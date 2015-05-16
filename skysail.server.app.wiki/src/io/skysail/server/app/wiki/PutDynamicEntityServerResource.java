package io.skysail.server.app.wiki;

import io.skysail.server.restlet.resources.PutEntityServerResource;

import org.apache.commons.beanutils.DynaBean;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import de.twenty11.skysail.server.beans.DynamicEntity;

public abstract class PutDynamicEntityServerResource <T extends DynamicEntity> extends PutEntityServerResource<T> {

    public T getData(Form form) {
        T entity = getEntity();
        DynaBean bean = entity.getInstance();
        for (Parameter parameter : form) {
            String name = parameter.getName();
            if (bean.getDynaClass().getDynaProperty(name) != null ) {
                bean.set(name, parameter.getValue());
            }
        }
        
        return entity;
    }

}
