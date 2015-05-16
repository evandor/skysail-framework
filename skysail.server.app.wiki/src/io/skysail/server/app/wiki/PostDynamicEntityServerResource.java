package io.skysail.server.app.wiki;

import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.commons.beanutils.DynaBean;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import de.twenty11.skysail.server.beans.DynamicEntity;

public abstract class PostDynamicEntityServerResource<T extends DynamicEntity> extends PostEntityServerResource<T> {

    public T getData(Form form) {
        T entity = createEntityTemplate();
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
