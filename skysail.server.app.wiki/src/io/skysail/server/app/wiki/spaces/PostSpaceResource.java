package io.skysail.server.app.wiki.spaces;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;

import org.restlet.data.Form;

import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;

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
        return populate(createEntityTemplate(), form);
    }

    public SkysailResponse<?> addEntity(Space entity) {
        app.getRepository().add(entity);
        return new SkysailResponse<String>();
    }

}
