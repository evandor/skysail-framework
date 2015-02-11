package io.skysail.server.ext.apt.test.withlist.folders;

import java.util.List;
import java.util.function.Consumer;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.apt.test.withlist.folders.*;


import de.twenty11.skysail.api.responses.Linkheader;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class FoldersResource extends ListServerResource<Folder> {

    private String id;

    public FoldersResource() {
        super(FolderResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Folders");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
    }

    @Override
    public List<Folder> getData() {
        return FoldersRepository.getInstance().getFolders();
    }

    @Override
    public List<Linkheader> getLinkheader() {
        return super.getLinkheader(PostFolderResource.class);
    }

    @Override
    public Consumer<? super Linkheader> getPathSubstitutions() {
        return l -> { l.substitute("id", id); };
    }
}
