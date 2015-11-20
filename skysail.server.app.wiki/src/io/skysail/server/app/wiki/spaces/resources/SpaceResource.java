package io.skysail.server.app.wiki.spaces.resources;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.wiki.WikiApplication;
import io.skysail.server.app.wiki.pages.resources.PagesResource;
import io.skysail.server.app.wiki.spaces.Space;
import io.skysail.server.restlet.resources.EntityServerResource;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.restlet.data.Status;

public class SpaceResource extends EntityServerResource<Space> {

    private String id;
    private WikiApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (WikiApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        Space space = app.getSpacesRepo().findOne(id);
        if (space.getPages().size() > 0) {
            throw new IllegalArgumentException("a space with at least one page cannot be deleted");
        }
        app.getSpacesRepo().delete(id);
        return new SkysailResponse<String>();
    }

    @Override
    public Space getEntity() {
         Space space = app.getSpacesRepo().findOne(id);
         String username = SecurityUtils.getSubject().getPrincipal().toString();
         if (!space.getOwner().equals(username)) {
             getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
             return null;
         }
         return space;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutSpaceResource.class, PagesResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(SpacesResource.class);
    }

}
