package io.skysail.server.app.wiki.pages.resources;

import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;

import java.util.List;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class SubpagesResource extends PagesResource {

    private String pageId;

    public SubpagesResource() {
   //     super(PageResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list SubPages");
    }

    @Override
    protected void doInit() throws ResourceException {
        pageId = getAttribute("pageId");
        app = (WikiApplication) getApplication();
    }

    @Override
    public List<Page> getEntity() {
        Page page = app.getPagesRepo().findOne(pageId);
        return null;//page.getSubpages();

    }
}
