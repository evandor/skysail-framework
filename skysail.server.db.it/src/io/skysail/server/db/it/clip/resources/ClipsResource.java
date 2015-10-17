package io.skysail.server.db.it.clip.resources;

import io.skysail.server.db.it.*;
import io.skysail.server.db.it.clip.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class ClipsResource extends ListServerResource<Clip> {

    private ClipApplication app;

    @Override
    protected void doInit() {
        app = (ClipApplication) getApplication();
    }

    @Override
    public List<Clip> getEntity() {
      return app.getRepository().find(new Filter(getRequest()));
    }

}
