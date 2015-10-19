package io.skysail.server.db.it.one2many.todo.resources;

import io.skysail.server.db.it.one2many.todo.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class TodosResource extends ListServerResource<Todo> {

    private TodoApplication app;

    @Override
    protected void doInit() {
        app = (TodoApplication) getApplication();
    }

    @Override
    public List<Todo> getEntity() {
      return app.getRepository().findVertex(new Filter(getRequest()));
    }

}
