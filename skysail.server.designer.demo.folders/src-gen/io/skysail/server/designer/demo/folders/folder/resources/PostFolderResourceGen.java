package io.skysail.server.designer.demo.folders.folder.resources;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;
import io.skysail.server.designer.demo.folders.*;
import io.skysail.server.designer.demo.folders.folder.*;

/**
 * generated from postResource.stg
 */
public class PostFolderResourceGen extends PostEntityServerResource<io.skysail.server.designer.demo.folders.folder.Folder> {

	protected FoldersApplication app;

    public PostFolderResourceGen() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (FoldersApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.folders.folder.Folder createEntityTemplate() {
        return new Folder();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.folders.folder.Folder entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.folders.folder.Folder.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(FoldersResourceGen.class);
    }
}