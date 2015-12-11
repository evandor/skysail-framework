package io.skysail.server.designer.checklist;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.api.links.Link;

import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.core.restlet.ResourceContextId;

public class TemplatesResource extends ListServerResource<io.skysail.server.designer.checklist.Template> {

    private ChecklistApplication app;
    private TemplateRepository repository;

    public TemplatesResource() {
        super(TemplateResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Templates");
    }

    @Override
    protected void doInit() {
        app = (ChecklistApplication) getApplication();
        repository = (TemplateRepository) app.getRepository(io.skysail.server.designer.checklist.Template.class);
    }

    @Override
    public List<io.skysail.server.designer.checklist.Template> getEntity() {
       return repository.find(new Filter(getRequest()));
    }

    public List<Link> getLinks() {
       return super.getLinks(PostTemplateResource.class);
    }
}