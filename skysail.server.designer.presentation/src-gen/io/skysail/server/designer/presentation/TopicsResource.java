package io.skysail.server.designer.presentation;

import java.util.List;
import io.skysail.api.links.Link;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;
import de.twenty11.skysail.server.core.restlet.ResourceContextId;

import javax.annotation.Generated;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class TopicsResource extends ListServerResource<Topic> {

    private InteractivePresentationApplication app;
    private TopicRepo repository;

    public TopicsResource() {
        super(TopicResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "List of Topics");
    }

    protected void doInit() {
        super.doInit();
        app = (InteractivePresentationApplication)getApplication();
        repository = (TopicRepo) app.getRepository(Topic.class);
    }

    @Override
    public List<Topic> getEntity() {
        return repository.find(new Filter());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
}
