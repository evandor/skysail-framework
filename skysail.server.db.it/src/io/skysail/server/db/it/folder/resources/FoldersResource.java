package io.skysail.server.db.it.folder.resources;

import io.skysail.server.db.it.folder.*;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

import java.util.List;

public class FoldersResource extends ListServerResource<Folder> {

    private FolderApplication app;

    @Override
    protected void doInit() {
        app = (FolderApplication) getApplication();
    }

    @Override
    public List<Folder> getEntity() {
      return app.getRepository().find(new Filter(getRequest()));
    }

}
