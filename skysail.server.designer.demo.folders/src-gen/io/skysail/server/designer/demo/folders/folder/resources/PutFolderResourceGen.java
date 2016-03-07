package io.skysail.server.designer.demo.folders.folder.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

import java.util.Date;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.folders.*;
import io.skysail.server.designer.demo.folders.folder.*;

/**
 * generated from putResource.stg
 */
public class PutFolderResourceGen extends PutEntityServerResource<io.skysail.server.designer.demo.folders.folder.Folder> {


    protected String id;
    protected FoldersApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (FoldersApplication)getApplication();
    }

    @Override
    public void updateEntity(Folder  entity) {
        io.skysail.server.designer.demo.folders.folder.Folder original = getEntity();
        copyProperties(original,entity);

        app.getRepository(io.skysail.server.designer.demo.folders.folder.Folder.class).update(original,app.getApplicationModel());
    }

    @Override
    public io.skysail.server.designer.demo.folders.folder.Folder getEntity() {
        return (io.skysail.server.designer.demo.folders.folder.Folder)app.getRepository(io.skysail.server.designer.demo.folders.folder.Folder.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(FoldersResourceGen.class);
    }
}