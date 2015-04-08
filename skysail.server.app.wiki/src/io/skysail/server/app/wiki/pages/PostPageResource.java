package io.skysail.server.app.wiki.pages;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.commons.beanutils.DynaBean;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PostPageResource extends PostEntityServerResource<Page> {

    private WikiApplication app;
    
    public PostPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "create new Space");
    }

    @Override
    protected void doInit() {
        app = (WikiApplication) getApplication();
    }

    @Override
    public Page createEntityTemplate() {
        return new Page();
    }

    public Page getData(Form form) {
        Page space = createEntityTemplate();
        DynaBean bean = space.getInstance();
        for (Parameter parameter : form) {
            String name = parameter.getName();
            if (bean.getDynaClass().getDynaProperty(name) != null ) {
                bean.set(name, parameter.getValue());
            }
        }
        
        return space;
    }

    public SkysailResponse<?> addEntity(Page entity) {
        app.getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
