package io.skysail.server.ext.apt.test.withlist.folders;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.api.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.PutEntityServerResource;

public class PutFolderResource extends PutEntityServerResource<Folder> {

	private String id;

	@Override
	protected void doInit() throws ResourceException {
		id = getAttribute("id");
	}

	@Override
	public Folder getEntity() {
		return FoldersRepository.getInstance().getById(id);
	}

	@Override
	public SkysailResponse<?> updateEntity(Folder entity) {
		FoldersRepository.getInstance().update(entity);
		return null;
	}

}
