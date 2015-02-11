package io.skysail.server.ext.apt.test.withlist.folders;

import java.util.List;

import org.restlet.resource.ResourceException;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class FolderResource extends EntityServerResource<Folder> {

	private String id;

	@Override
	protected void doInit() throws ResourceException {
	    id = getAttribute("id");
	}

	@Override
	public Folder getData() {
		return FoldersRepository.getInstance().getById(id);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
        FoldersRepository.getInstance().delete(id);
        return new SkysailResponse<String>();
	}

    @Override
	public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PutFolderResource.class);
	}

}
