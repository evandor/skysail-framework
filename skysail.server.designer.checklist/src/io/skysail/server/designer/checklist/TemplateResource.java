package io.skysail.server.designer.checklist;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TemplateResource extends EntityServerResource<io.skysail.server.designer.checklist.Template> {

    private String id;
    private ChecklistApplication app;
    private TemplateRepository repository;

    public TemplateResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (ChecklistApplication) getApplication();
        repository = (TemplateRepository) app.getRepository(io.skysail.server.designer.checklist.Template.class);
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        repository.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public io.skysail.server.designer.checklist.Template getEntity() {
        return (io.skysail.server.designer.checklist.Template)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutTemplateResource.class);
    }

}