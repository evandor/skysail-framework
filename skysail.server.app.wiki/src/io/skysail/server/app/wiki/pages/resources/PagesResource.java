package io.skysail.server.app.wiki.pages.resources;

import io.skysail.api.links.Link;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.Page;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.utils.OrientDbUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class PagesResource extends ListServerResource<Map<String, Object>> {

    private String spaceId;
    private WikiApplication app;

    public PagesResource() {
        super(PageResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Pages");
    }

    @Override
    protected void doInit() throws ResourceException {
        spaceId = getAttribute("id");
        app = (WikiApplication) getApplication();
    }

    @Override
    public List<Map<String, Object>> getEntity() {
        Space space = app.getRepository().getSpaceById(spaceId);
        Map<String, Object> map = OrientDbUtils.toMap(space);
        List<String> pages = (List<String>) map.get(Page.class.getSimpleName());
        return pages.stream().map(pageId -> {
            return app.getRepository().getPageById(pageId);
        }).map(page -> {
            return OrientDbUtils.toMap(page);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostPageResource.class, PagesResource.class);
    }
}
