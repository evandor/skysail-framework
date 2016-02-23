package io.skysail.server.designer.demo.folders;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;

public class PutFolderResource extends PutEntityServerResource<io.skysail.server.designer.demo.folders.Folder> {


    protected String id;
    protected FoldersApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (FoldersApplication)getApplication();
    }

    @Override
    public void updateEntity(Folder  entity) {
        io.skysail.server.designer.demo.folders.Folder original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.folders.Folder.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.folders.Folder getEntity() {
        return (io.skysail.server.designer.demo.folders.Folder)app.getRepository(io.skysail.server.designer.demo.folders.Folder.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(FoldersResource.class);
    }
}