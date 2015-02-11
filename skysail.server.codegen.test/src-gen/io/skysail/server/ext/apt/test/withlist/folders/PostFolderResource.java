package io.skysail.server.ext.apt.test.withlist.folders;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.withlist.folders.*;


import de.twenty11.skysail.api.responses.SkysailResponse;
import io.skysail.server.ext.apt.test.withlist.CompaniesGen;
import de.twenty11.skysail.server.core.restlet.PostEntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;
import de.twenty11.skysail.server.core.restlet.ServerLink;

public class PostFolderResource extends PostEntityServerResource<Folder> {

    private String id;

	private CompaniesGen app;

	public PostFolderResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new Folder");
    }

    @Override
	protected void doInit() throws ResourceException {
		app = (CompaniesGen)getApplication();
		id = getAttribute("id");
	}

	@Override
    public Folder createEntityTemplate() {
	    return new Folder();
    }

	@Override
    public SkysailResponse<?> addEntity(Folder entity) {
		entity = FoldersRepository.getInstance().add(entity);
	    return new SkysailResponse<String>();
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(FoldersResource.class);
	}
}