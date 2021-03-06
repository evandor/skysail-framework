package io.skysail.server.db.it.clip.resources;

import io.skysail.server.db.it.clip.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class ClipsResource extends ListServerResource<Clip> {

    private ClipApplication app;
    private ClipRepository repository;

    @Override
    protected void doInit() {
        app = (ClipApplication) getApplication();
        repository = (ClipRepository) app.getRepository(Clip.class);
    }

    @Override
    public List<Clip> getEntity() {
      return repository.find(new Filter(getRequest()));
    }

}
