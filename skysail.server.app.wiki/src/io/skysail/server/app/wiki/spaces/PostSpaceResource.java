package io.skysail.server.app.wiki.spaces;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.commons.beanutils.DynaBean;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

public class PostSpaceResource extends PostEntityServerResource<Space> {

    private WikiApplication app;

    @Override
    protected void doInit() {
        app = (WikiApplication) getApplication();
    }

    @Override
    public Space createEntityTemplate() {
        return new Space();
    }

    public Space getData(Form form) {
        Space space = createEntityTemplate();
        DynaBean bean = space.getInstance();
        for (Parameter parameter : form) {
            String name = parameter.getName();
            if (bean.getDynaClass().getDynaProperty(name) != null ) {
                bean.set(name, parameter.getValue());
            }
        }
        
        return space;
    }

    public SkysailResponse<?> addEntity(Space entity) {
        app.getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
