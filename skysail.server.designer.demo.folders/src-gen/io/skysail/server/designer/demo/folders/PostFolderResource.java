package io.skysail.server.designer.demo.folders;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

/**
 * generated from postResource.stg
 */
public class PostFolderResource extends PostEntityServerResource<io.skysail.server.designer.demo.folders.Folder> {

	protected FoldersApplication app;

    public PostFolderResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (FoldersApplication) getApplication();
    }

    @Override
    public io.skysail.server.designer.demo.folders.Folder createEntityTemplate() {
        return new Folder();
    }

    @Override
    public void addEntity(io.skysail.server.designer.demo.folders.Folder entity) {
        Subject subject = SecurityUtils.getSubject();
        String id = app.getRepository(io.skysail.server.designer.demo.folders.Folder.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(FoldersResource.class);
    }
}