package io.skysail.server.app.wiki;

import java.util.Date;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.repos.Repository;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.PostEntityServerResource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

public class PostPageResource extends PostEntityServerResource<io.skysail.server.app.wiki.Page> {

	private WikiApplication app;
    private Repository repository;

    public PostPageResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new ");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (WikiApplication) getApplication();
        repository = app.getRepository(Space.class);
    }

    @Override
    public io.skysail.server.app.wiki.Page createEntityTemplate() {
        return new Page();
    }

    @Override
    public void addEntity(io.skysail.server.app.wiki.Page entity) {
        Subject subject = SecurityUtils.getSubject();

        io.skysail.server.app.wiki.Space entityRoot = (io.skysail.server.app.wiki.Space) repository.findOne(getAttribute("id"));
        entityRoot.getPages().add(entity);
        repository.update(entityRoot, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(PagesResource.class);
    }
}